package com.gyg.listen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gyg.config.RabbitMqConfig;
import com.gyg.modle.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author by gyg
 * @date 2021/12/29 22:14
 * @description
 */
@Slf4j
@Component
public class MessageListen {
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = RabbitMqConfig.NORMAL_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMqConfig.NORMAL_ROUTING,
            value = @Queue(value = RabbitMqConfig.NORMAL_QUEUE,
                    arguments = {@Argument(name = "x-max-length", value = "6", type = "java.lang.Integer")}))
    )
    public void consumer(String message) {
        log.info("rabbitmq接收到消息：{}", message);
    }

    /**
     * <p>创建一个消费者 模拟处理出现异常的情况</p>
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "exception_queue",
                    arguments = {@Argument(name = "x-dead-letter-exchange", value = RabbitMqConfig.DIE_QUEUE, type = "java.lang.String"),
                            @Argument(name = "x-dead-letter-routing-key", value = RabbitMqConfig.DIE_ROUTING, type = "java.lang.String")}),
            exchange = @Exchange("exception_exchange"),
            key = "exception_routing"
    ))
    public void exceptionConsumer(Message message) {
        String body = new String(message.getBody(), Charset.defaultCharset());
        MessageProperties properties = message.getMessageProperties();
        try {
            String consumerTag = properties.getConsumerTag();
            String user = properties.getHeader("user").toString();
            log.info("异常消费者接收到消息 msg：{} user:{}", body, user);
            MessageVO messageVO = JSON.parseObject(body, MessageVO.class);
            if (messageVO.getVersion() == 1) {
                messageVO.setRetry(messageVO.getRetry() + 1);
                throw new RuntimeException("消息版本不对，手动回滚");
            }
        } catch (Exception e) {
            Integer retry = properties.getHeader("retry-times");
//            if (retry != null && retry >= 1) {
//                // 重试3次依然错误的消息，设置过期到死信队列
//            }
            if (retry == null) {
                properties.setHeader("retry-times", 1);
            } else {
                properties.setHeader("retry-times", retry + 1);
            }
            log.error("出现异常 消息回滚提示 error:{}", e.getCause().getMessage());
        }

    }
}
