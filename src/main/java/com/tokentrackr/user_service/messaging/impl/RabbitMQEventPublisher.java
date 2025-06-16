package com.tokentrackr.user_service.messaging.impl;

import com.tokentrackr.user_service.dto.events.BalanceUpdateEvent;
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

    @Override
    public void publishBalanceUpdated(BalanceUpdatedEvent event) {
        rabbitTemplate.convertAndSend("balance.updated.queue", event);
    }

    @Override
    public void publishBalanceUpdateFailed(BalanceUpdateFailedEvent event) {
        rabbitTemplate.convertAndSend("balance.update.failed.queue", event);
    }
}
