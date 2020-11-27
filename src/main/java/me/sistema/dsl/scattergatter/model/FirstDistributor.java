package me.sistema.dsl.scattergatter.model;

import java.util.List;

public class FirstDistributor extends GenericDistributor {

    public FirstDistributor() {
    }

    public FirstDistributor(String orderNumber, List<Item> items) {
        super(orderNumber, items);
    }
}
