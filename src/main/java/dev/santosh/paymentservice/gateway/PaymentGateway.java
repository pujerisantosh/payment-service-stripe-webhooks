package dev.santosh.paymentservice.gateway;

import dev.santosh.paymentservice.domain.entity.PaymentIntent;

import java.math.BigDecimal;

public interface PaymentGateway {
    String initiate(PaymentIntent paymentIntent);

    String initiate(BigDecimal amount, String currency, String idempotencyKey);
}
