package dev.santosh.paymentservice.service;

import dev.santosh.paymentservice.domain.entity.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Component
public class PaymentStateTransitionValidator {

    private static final Map<PaymentStatus, Set<PaymentStatus>> ALLOWED_TRANSITIONS =
            Map.of(
                    PaymentStatus.CREATED,
                    EnumSet.of(
                            PaymentStatus.SUCCESS,
                            PaymentStatus.FAILED,
                            PaymentStatus.CANCELLED
                    )
            );

    public void validate(PaymentStatus current, PaymentStatus next) {
        if (current == next) {
            return; // idempotent update
        }

        Set<PaymentStatus> allowed = ALLOWED_TRANSITIONS.get(current);

        if (allowed == null || !allowed.contains(next)) {
            throw new IllegalStateException(
                    "Invalid payment state transition: " + current + " → " + next
            );
        }
    }
}
