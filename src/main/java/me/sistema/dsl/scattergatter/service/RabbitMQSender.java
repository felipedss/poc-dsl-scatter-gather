package me.sistema.dsl.scattergatter.service;

import me.sistema.dsl.scattergatter.model.Order;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.pattern.routing-key}")
    private String routingkey;

    public void send(Order order) {
        rabbitTemplate.convertAndSend(exchange, routingkey, order);
    }

}
