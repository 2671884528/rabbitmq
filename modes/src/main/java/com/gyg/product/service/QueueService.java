package com.gyg.product.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author by gyg
 * @date 2021/12/13 22:54
 * @description 直接队列
 */
@Service
public class QueueService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void sendDirectMessage(String message){
        //
        rabbitTemplate.convertAndSend("gyg.default.queue",message);
    }
}
