package me.sistema.dsl.scattergatter.model;

import java.util.List;

public class Delivery {

    private boolean error;
    private List<GenericDistributor> items;

    public Delivery(boolean error, List<GenericDistributor> items) {
        this.error = error;
        this.items = items;
    }

    public List<GenericDistributor> getItems() {
        return items;
    }

    public void setItems(List<GenericDistributor> items) {
        this.items = items;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "items=" + items +
                '}';
    }
}
