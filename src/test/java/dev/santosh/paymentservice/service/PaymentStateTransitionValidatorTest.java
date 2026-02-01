package dev.santosh.paymentservice.service;

import dev.santosh.paymentservice.domain.entity.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStateTransitionValidatorTest {

    private PaymentStateTransitionValidator validator;

    @BeforeEach
    void setup() {
        validator = new PaymentStateTransitionValidator();
    }

    @Test
    void shouldAllowCreatedToSuccess() {
        assertDoesNotThrow(() ->
                validator.validate(PaymentStatus.CREATED, PaymentStatus.SUCCESS)
        );
    }

    @Test
    void shouldRejectSuccessToCreated() {
        assertThrows(IllegalStateException.class, () ->
                validator.validate(PaymentStatus.SUCCESS, PaymentStatus.CREATED)
        );
    }
}
