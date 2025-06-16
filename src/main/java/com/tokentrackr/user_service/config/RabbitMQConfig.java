package com.tokentrackr.user_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // DirectExchange for balance commands/responses
    public static final String SAGA_DIRECT_EXCHANGE            = "saga.direct.exchange";
    public static final String BALANCE_UPDATE_QUEUE            = "balance.update.queue";
    public static final String BALANCE_UPDATED_QUEUE           = "balance.updated.queue";
    public static final String BALANCE_UPDATE_FAILED_QUEUE     = "balance.update.failed.queue";

    @Bean
    DirectExchange sagaDirectExchange() {
        return new DirectExchange(SAGA_DIRECT_EXCHANGE);
    }
    @Bean Queue balanceUpdateQueue()        { return QueueBuilder.durable(BALANCE_UPDATE_QUEUE).build(); }
    @Bean Queue balanceUpdatedQueue()       { return QueueBuilder.durable(BALANCE_UPDATED_QUEUE).build(); }
    @Bean Queue balanceUpdateFailedQueue()  { return QueueBuilder.durable(BALANCE_UPDATE_FAILED_QUEUE).build(); }

    @Bean Binding bindBalanceUpdate() {
        return BindingBuilder.bind(balanceUpdateQueue())
                .to(sagaDirectExchange())
                .with("balance.update");
    }
    @Bean Binding bindBalanceUpdated() {
        return BindingBuilder.bind(balanceUpdatedQueue())
                .to(sagaDirectExchange())
                .with("balance.updated");
    }
    @Bean Binding bindBalanceUpdateFailed() {
        return BindingBuilder.bind(balanceUpdateFailedQueue())
                .to(sagaDirectExchange())
                .with("balance.update.failed");
    }

    // TopicExchange for final saga outcomes
    public static final String SAGA_TOPIC_EXCHANGE             = "saga.topic.exchange";

    @Bean TopicExchange sagaTopicExchange() {
        return new TopicExchange(SAGA_TOPIC_EXCHANGE);
    }
}
