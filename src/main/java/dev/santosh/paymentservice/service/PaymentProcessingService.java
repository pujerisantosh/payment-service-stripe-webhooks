
package dev.santosh.paymentservice.service;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import dev.santosh.paymentservice.domain.entity.PaymentStatus;
import dev.santosh.paymentservice.gateway.PaymentGateway;
import dev.santosh.paymentservice.repository.PaymentIntentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentProcessingService {

    private final PaymentGateway paymentGateway;
    private final PaymentIntentRepository repository;

    public PaymentProcessingService(
            @Qualifier("stripe") PaymentGateway paymentGateway,
            PaymentIntentRepository repository
    ) {
        this.paymentGateway = paymentGateway;
        this.repository = repository;
    }

    public PaymentIntent createPayment(
            BigDecimal amount,
            String currency,
            String idempotencyKey
    ) {
        // 1️⃣ Create local payment
        PaymentIntent intent = new PaymentIntent(amount, currency);
        intent.setIdempotencyKey(idempotencyKey);
        intent.setStatus(PaymentStatus.CREATED);

        repository.save(intent);

        // 2️⃣ Call Stripe
        String gatewayRef = paymentGateway.initiate(intent);

        // 3️⃣ Update with provider reference
        intent.setGatewayReferenceId(gatewayRef);

        return repository.save(intent);
    }
}
