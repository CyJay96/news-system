package ru.clevertec.ecl.authservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mp.rabbitmq.delay.exchange}")
    private String exchange;

    @Value("${mp.rabbitmq.delay.routing-key}")
    private String routingkey;

    public void send(String username){
        rabbitTemplate.convertAndSend(exchange, routingkey, username);
        log.info("User with username {} successfully send", username);
    }
}
