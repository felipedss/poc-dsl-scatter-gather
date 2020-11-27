package me.sistema.dsl.scattergatter.model;

public class Order {

    private String id;
    private FirstDistributor firstDistributor;
    private SecondDistributor secondDistributor;

    public Order() {
    }

    public Order(String id, FirstDistributor firstDistributor, SecondDistributor secondDistributor) {
        this.id = id;
        this.firstDistributor = firstDistributor;
        this.secondDistributor = secondDistributor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FirstDistributor getFirstDistributor() {
        return firstDistributor;
    }

    public void setFirstDistributor(FirstDistributor firstDistributor) {
        this.firstDistributor = firstDistributor;
    }

    public SecondDistributor getSecondDistributor() {
        return secondDistributor;
    }

    public void setSecondDistributor(SecondDistributor secondDistributor) {
        this.secondDistributor = secondDistributor;
    }
}
