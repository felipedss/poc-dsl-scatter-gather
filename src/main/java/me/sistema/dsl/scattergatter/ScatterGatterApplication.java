package me.sistema.dsl.scattergatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class ScatterGatterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScatterGatterApplication.class, args);
    }

}
