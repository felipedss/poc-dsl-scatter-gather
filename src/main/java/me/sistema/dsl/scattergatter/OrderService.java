package me.sistema.dsl.scattergatter;

import me.sistema.dsl.scattergatter.model.FirstDistributor;
import me.sistema.dsl.scattergatter.model.SecondDistributor;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private int count;

    public FirstDistributor prepareOrder(FirstDistributor firstDistributor) {
        System.out.println("First distributor started");

        firstDistributor.getItems()
                .forEach(v -> {
                    count();
                    System.out.println(count);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        System.out.println("First distributor finished");
        return firstDistributor;
    }

    public SecondDistributor prepareOrder(SecondDistributor secondDistributor) {
        System.out.println("Second distributor started");

        secondDistributor.getItems()
                .forEach(v -> {
                    try {
                        count();
                        System.out.println(count);
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println("First distributor finished");
        return secondDistributor;
    }

    synchronized void count() {
        count++;
    }

}
