package me.sistema.dsl.scattergatter;

import me.sistema.dsl.scattergatter.model.Order;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface OrderGatewayService {

    @Gateway(requestChannel = "orders.input")
    void placeOrder(Order orders);

}
