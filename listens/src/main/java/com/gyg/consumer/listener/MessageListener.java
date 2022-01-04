package com.gyg.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author by gyg
 * @date 2021/12/13 23:08
 * @description
 */
@Service
@Slf4j
public class MessageListener {

    /**
     * <p>不设置交换机、routingKey，会使用默认的交换机，路由用队列名</p>
     * @param json
     */
    @RabbitListener(queuesToDeclare = @Queue(value = "gyg.default.queue"))
    public void receiverMessage(String json) {
            log.info("获取队列的消息:{}",json);
    }
}
