package dev.santosh.paymentservice.gateway;

import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component("dummyPaymentGateway")
public class DummyPaymentGateway implements PaymentGateway {

    @Override
    public String initiate(PaymentIntent paymentIntent) {
        return "DUMMY-" + UUID.randomUUID();
    }

    @Override
    public String initiate(BigDecimal amount, String currency, String idempotencyKey) {
        return "";
    }
}
