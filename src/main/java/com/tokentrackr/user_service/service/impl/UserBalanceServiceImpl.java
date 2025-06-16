package com.tokentrackr.user_service.service.impl;

import com.tokentrackr.user_service.dto.events.BalanceUpdateEvent;
import com.tokentrackr.user_service.dto.events.BalanceUpdateFailedEvent;
import com.tokentrackr.user_service.dto.events.BalanceUpdatedEvent;
import com.tokentrackr.user_service.enums.TransactionType;
import com.tokentrackr.user_service.exception.InsufficientBalanceException;
import com.tokentrackr.user_service.exception.UserNotFoundException;
import com.tokentrackr.user_service.messaging.EventPublisher;
import com.tokentrackr.user_service.repository.UserRepository;
import com.tokentrackr.user_service.repository.entity.UserEntity;
import com.tokentrackr.user_service.service.interfaces.UserBalanceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserBalanceServiceImpl implements UserBalanceService {
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    @Override
    public void processBalanceUpdate(BalanceUpdateEvent event) {
        try {
            UserEntity user = userRepository.findByKeycloakId(event.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(event.getUserId()));

            if (event.isCompensation()) {
                reverseBalanceChange(user, event);
            } else {
                applyBalanceChange(user, event);
            }

            publishSuccess(event);
        } catch (Exception ex) {
            publishFailure(event, ex.getMessage());
        }
    }

    private void applyBalanceChange(UserEntity user, BalanceUpdateEvent event) {
        BigDecimal amount = event.getAmount();
        if (event.getTransactionType() == TransactionType.BUY) {
            if (user.getBalance().compareTo(amount) < 0) {
                throw new InsufficientBalanceException("Insufficient funds");
            }
            user.setBalance(user.getBalance().subtract(amount));
        } else {
            user.setBalance(user.getBalance().add(amount));
        }
        userRepository.save(user);
    }

    private void reverseBalanceChange(UserEntity user, BalanceUpdateEvent event) {
        BigDecimal amount = event.getAmount();
        if (event.getTransactionType() == TransactionType.BUY) {
            user.setBalance(user.getBalance().add(amount)); // Compensate BUY
        } else {
            user.setBalance(user.getBalance().subtract(amount)); // Compensate SELL
        }
        userRepository.save(user);
    }

    private void publishSuccess(BalanceUpdateEvent event) {
        BalanceUpdatedEvent successEvent = BalanceUpdatedEvent.builder()
                .sagaId(event.getSagaId())
                .transactionId(event.getTransactionId())
                .userId(event.getUserId())
                .success(true)
                .build();

        eventPublisher.publishBalanceUpdated(successEvent);
    }

    private void publishFailure(BalanceUpdateEvent event, String reason) {
        BalanceUpdateFailedEvent failedEvent = BalanceUpdateFailedEvent.builder()
                .sagaId(event.getSagaId())
                .transactionId(event.getTransactionId())
                .userId(event.getUserId())
                .failureReason(reason)
                .build();

        eventPublisher.publishBalanceUpdateFailed(failedEvent);
    }
}
