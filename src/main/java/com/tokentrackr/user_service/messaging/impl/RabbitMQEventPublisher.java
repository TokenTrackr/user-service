package com.tokentrackr.user_service.messaging.impl;

import com.tokentrackr.user_service.dto.events.BalanceUpdateFailedEvent;
import com.tokentrackr.user_service.dto.events.BalanceUpdatedEvent;
import com.tokentrackr.user_service.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RabbitMQEventPublisher implements EventPublisher {
    private final RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE = "saga.direct.exchange";

    @Override
    public void publishBalanceUpdated(BalanceUpdatedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "balance.updated", event);
    }

    @Override
    public void publishBalanceUpdateFailed(BalanceUpdateFailedEvent event) {
        rabbitTemplate.convertAndSend(EXCHANGE, "balance.update.failed", event);
    }
}
