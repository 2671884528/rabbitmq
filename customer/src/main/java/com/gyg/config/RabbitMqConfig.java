package com.gyg.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by gyg
 * @date 2021/12/29 21:32
 */
@Configuration
public class RabbitMqConfig {
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DIE_EXCHANGE = "die_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DIE_QUEUE = "die_queue";
    public static final String NORMAL_ROUTING = "normal_routing";
    public static final String DIE_ROUTING = "die_routing";
//
//    @Bean(DIE_EXCHANGE)
//    public TopicExchange createDieExchange() {
//        return new TopicExchange(DIE_EXCHANGE);
//    }
//    @Bean(DIE_QUEUE)
//    public Queue createDieQueue() {
//        // 交换机参数，【写死，可百度查询相应的参数】
//        return new Queue(DIE_QUEUE, true, false, false);
//    }
//    @Bean
//    public Binding bindingDie(@Qualifier(DIE_EXCHANGE) TopicExchange exchange,
//                              @Qualifier(DIE_QUEUE) Queue queue) {
//        return BindingBuilder.bind(createDieQueue()).to(createDieExchange()).with(DIE_ROUTING);
//    }

    private final RabbitTemplate rabbitTemplate;

    public RabbitMqConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public MessageRecoverer messageRecoverer(){
        return new RepublishMessageRecoverer(rabbitTemplate,DIE_EXCHANGE,DIE_ROUTING);
    }
}
