package me.sistema.dsl.scattergatter;

import me.sistema.dsl.scattergatter.model.SecondDistributor;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.AggregatingMessageHandler;
import org.springframework.integration.aggregator.MethodInvokingCorrelationStrategy;
import org.springframework.integration.aggregator.MethodInvokingMessageGroupProcessor;
import org.springframework.integration.aggregator.MethodInvokingReleaseStrategy;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;
import org.springframework.integration.router.RecipientListRouter;
import org.springframework.integration.scattergather.ScatterGatherHandler;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Arrays;
import java.util.concurrent.Executors;

@Configuration
public class MainFlowConfiguration {

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private OrderAggregator orderAggregator;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ResultService resultService;

    @Autowired
    private BeanFactory beanFactory;

    @Bean
    public MessageHandler distributor() {
        RecipientListRouter router = new RecipientListRouter();
        router.setApplySequence(true);
        router.setChannels(Arrays.asList(firstDistributorChannel(),
                secondDistributorChannel()));
        return router;
    }

    @Bean
    public ExecutorChannel firstDistributorChannel() {
        return MessageChannels.executor("distribution1-flow", Executors.newCachedThreadPool())
                .get();
    }

    @Bean
    public MessageChannel secondDistributorChannel() {
        return MessageChannels.executor("distribution2-flow", Executors.newCachedThreadPool())
                .get();
    }

    @Bean
    public IntegrationFlow firstDistributorFlow() {
        return IntegrationFlows
                .from(firstDistributorChannel())
                .split("payload.firstDistributor")
                .handle(orderService, "prepareOrder", c -> c.advice(expressionAdvice()))
                .get();
    }

    @Bean
    public IntegrationFlow secondDistributorFlow() {
        return IntegrationFlows
                .from(secondDistributorChannel())
                .split("payload.secondDistributor")
                .handle(orderService, "prepareOrder", c -> c.advice(expressionAdvice()))
                .get();
    }

    @Bean
    public MessageHandler gatherer() {
        return new AggregatingMessageHandler(
                new MethodInvokingMessageGroupProcessor(orderAggregator, "output"),
                new SimpleMessageStore(),
                new MethodInvokingCorrelationStrategy(orderAggregator, "correlateBy"),
                new MethodInvokingReleaseStrategy(orderAggregator, "releaseChecker"));
    }

    @Bean
    @ServiceActivator(inputChannel = "distributionChannel")
    public MessageHandler scatterGatherDistribution() {
        ScatterGatherHandler handler = new ScatterGatherHandler(distributor(), gatherer());
        handler.setOutputChannel(output());
        return handler;
    }

    @ServiceActivator(inputChannel = "scatterGatherErrorChannel")
    public void processAsyncScatterError(MessagingException payload) {
        final SecondDistributor secondDistributor = (SecondDistributor) payload.getFailedMessage().getPayload();
        secondDistributor.setError(true);
    }

    @Bean
    public MessageChannel output() {
        return MessageChannels.executor("output-flow", Executors.newCachedThreadPool()).get();
    }

    @Bean(name = "order")
    public IntegrationFlow orders(ConnectionFactory connectionFactory) {
        final AmqpInboundChannelAdapterSMLCSpec messageProducerSpec = Amqp.inboundAdapter(connectionFactory, "scatter-gather-queue")
                .messageConverter(messageConverter);
        messageProducerSpec.configureContainer(consumer -> consumer.concurrentConsumers(4)
                .channelTransacted(true)
                .defaultRequeueRejected(false));
        return IntegrationFlows
                .from(messageProducerSpec)
                .handle(scatterGatherDistribution())
                .get();
    }


    @Bean
    public IntegrationFlow resultFlow() {
        return IntegrationFlows
                .from(output())
                .split("payload")
                .handle(resultService, "finish")
                .get();
    }

    @Bean
    public Advice expressionAdvice() {
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
        advice.setFailureChannelName("scatterGatherErrorChannel");
        advice.setReturnFailureExpressionResult(true);
        advice.setPropagateEvaluationFailures(true);
        advice.setTrapException(true);
        return advice;
    }


}
