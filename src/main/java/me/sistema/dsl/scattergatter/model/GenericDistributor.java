package me.sistema.dsl.scattergatter.model;

import java.util.List;

public abstract class GenericDistributor {

    protected String orderNumber;
    protected List<Item> items;
    protected boolean error;
    protected int count;

    public GenericDistributor() {
    }

    public GenericDistributor(String orderNumber, List<Item> items) {
        this.orderNumber = orderNumber;
        this.items = items;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
