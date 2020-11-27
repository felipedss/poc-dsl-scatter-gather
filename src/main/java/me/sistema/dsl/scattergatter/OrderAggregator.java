package me.sistema.dsl.scattergatter;

import me.sistema.dsl.scattergatter.model.Delivery;
import me.sistema.dsl.scattergatter.model.GenericDistributor;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.CorrelationStrategy;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderAggregator {

    @Aggregator
    public Delivery output(List<GenericDistributor> objects) {

        final boolean hasError = objects.stream()
                .anyMatch(GenericDistributor::isError);

        return new Delivery(hasError, objects);
    }

    @CorrelationStrategy
    public String correlateBy(Object object) {
        return ((GenericDistributor) object).getOrderNumber();
    }

    @ReleaseStrategy
    public boolean releaseChecker(List<Message<Object>> messages) {
        final Integer sequenceSize = (Integer) messages.get(0).getHeaders().get("sequenceSize");
        return messages.size() == 2;
    }

}