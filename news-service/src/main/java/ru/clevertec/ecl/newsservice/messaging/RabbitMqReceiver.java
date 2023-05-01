package ru.clevertec.ecl.newsservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.newsservice.model.entity.Username;
import ru.clevertec.ecl.newsservice.repository.UsernameRepository;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${mp.rabbitmq.queue}")
public class RabbitMqReceiver {

    private final UsernameRepository usernameRepository;

    @RabbitHandler
    public void receiver(String username) {
        log.info("User with username {} successfully received: " + username);
        usernameRepository.save(Username.builder().username(username).build());
        log.info("Username {} successfully saved: " + username);
    }
}
