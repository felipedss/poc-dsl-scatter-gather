package me.sistema.dsl.scattergatter.model;

import java.util.List;

public class SecondDistributor extends GenericDistributor {

    public SecondDistributor() {
    }

    public SecondDistributor(String orderNumber, List<Item> items) {
        super(orderNumber, items);
    }
}
