package me.sistema.dsl.scattergatter;

import me.sistema.dsl.scattergatter.model.Delivery;
import org.springframework.stereotype.Service;

@Service
public class ResultService {

    public void finish(Delivery delivery) {
        System.out.println("Resulado da entrega" + delivery.getItems());
    }

}
