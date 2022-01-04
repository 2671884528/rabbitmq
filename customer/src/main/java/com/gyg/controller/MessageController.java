package com.gyg.controller;

import com.alibaba.fastjson.JSON;
import com.gyg.config.RabbitMqConfig;
import com.gyg.modle.MessageVO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author by gyg
 * @date 2021/12/29 21:43
 * @description
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    public MessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/create")
    public void createMessage(@RequestBody MessageVO messageVO) {
        String messageJson = JSON.toJSONString(messageVO);
        // 发送并不会主动创建队列、需要有消费者来创建队列，或者代码创建
        // 【生产者需要在消费者之前创建，否则会出现无法启动消费者的情况，
        // 因为broker中还没有生产者推消息的消息缓存，消费者无法创建队列】
        rabbitTemplate.convertAndSend(RabbitMqConfig.NORMAL_EXCHANGE, RabbitMqConfig.NORMAL_ROUTING, messageJson, (message) -> {
            // 给消息附加消息头，一般场景只是简单的发送消息【敲代码，开心就好】
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setConsumerTag("gyg-专属");
            return message;
        });
    }

    @PostMapping("/exception")
    public void exceptionMessage(@RequestBody MessageVO messageVO) {
        String messageJson = JSON.toJSONString(messageVO);
        rabbitTemplate.convertAndSend("exception_exchange", "exception_routing", messageJson, (message) -> {
            // 自定义一些消息头，监听Message类就可以获【Message的消息体和消息头】
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.setContentType("application/json");
            messageProperties.setContentEncoding("UTF-8");
            // 会话唯一
            messageProperties.setConsumerTag("gyg-专属");
            messageProperties.setHeader("user","gyg加急消息");
            messageProperties.setMessageId(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16));
            return message;
        });
    }
}
