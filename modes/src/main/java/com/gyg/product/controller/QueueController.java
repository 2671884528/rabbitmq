package com.gyg.product.controller;

import com.gyg.product.service.QueueService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by gyg
 * @date 2021/12/13 22:59
 * @description
 */
@RestController
@RequestMapping("/")
@AllArgsConstructor
public class QueueController {

    private final QueueService queueService;

    @GetMapping("/queue")
    public void sendQueue(@RequestParam("message") String message) {
            queueService.sendDirectMessage(message);
    }
}
