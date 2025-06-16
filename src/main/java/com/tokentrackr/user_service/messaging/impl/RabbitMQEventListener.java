package com.tokentrackr.user_service.messaging.impl;

import com.tokentrackr.user_service.dto.events.BalanceUpdateEvent;
import com.tokentrackr.user_service.messaging.EventListener;
import com.tokentrackr.user_service.service.interfaces.UserBalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQEventListener implements EventListener {
    private final UserBalanceService userBalanceService;

    @Override
    @RabbitListener(queues = "balance.update.queue")
    public void handleBalanceUpdate(BalanceUpdateEvent event) {
        log.info("Received balance update event for user: {}", event.getUserId());
        userBalanceService.processBalanceUpdate(event);
    }
}
