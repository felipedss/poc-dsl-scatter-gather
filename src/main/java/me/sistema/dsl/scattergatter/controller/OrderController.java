package me.sistema.dsl.scattergatter.controller;

import me.sistema.dsl.scattergatter.model.FirstDistributor;
import me.sistema.dsl.scattergatter.model.Item;
import me.sistema.dsl.scattergatter.model.Order;
import me.sistema.dsl.scattergatter.model.SecondDistributor;
import me.sistema.dsl.scattergatter.service.RabbitMQSender;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/order")
public class OrderController {

    private final RabbitMQSender rabbitMQSender;

    public OrderController(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> post() {
        final String orderNumber = UUID.randomUUID().toString();

        List<Item> first = new ArrayList<>();
        for (int index = 0; index < 50; index++) {
            first.add(new Item());
        }

        List<Item> second = new ArrayList<>();
        for (int index = 0; index < 50; index++) {
            second.add(new Item());
        }

        final FirstDistributor firstDistributor = new FirstDistributor(orderNumber, first);
        final SecondDistributor secondDistributor = new SecondDistributor(orderNumber, second);

        final Order order = new Order(orderNumber, firstDistributor, secondDistributor);
        rabbitMQSender.send(order);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
