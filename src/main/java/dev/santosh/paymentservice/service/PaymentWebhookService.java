package dev.santosh.paymentservice.service;

import com.stripe.model.Event;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import dev.santosh.paymentservice.domain.entity.PaymentStatus;
import dev.santosh.paymentservice.repository.PaymentIntentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentWebhookService {

    private final PaymentIntentRepository repository;

    public PaymentWebhookService(PaymentIntentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void handleStripeEvent(Event event) {

        String type = event.getType();

        if (!type.startsWith("payment_intent.")) {
            return; // ignore non-payment events
        }

        String stripePaymentIntentId =
                event.getDataObjectDeserializer()
                        .getObject()
                        .map(obj -> ((com.stripe.model.PaymentIntent) obj).getId())
                        .orElseThrow(() -> new IllegalStateException("Invalid Stripe payload"));

        PaymentIntent intent = repository
                .findByGatewayReferenceId(stripePaymentIntentId)
                .orElseThrow(() -> new IllegalStateException("Payment not found"));

        switch (type) {
            case "payment_intent.succeeded" ->
                    intent.setStatus(PaymentStatus.SUCCESS);

            case "payment_intent.payment_failed" ->
                    intent.setStatus(PaymentStatus.FAILED);

            case "payment_intent.canceled" ->
                    intent.setStatus(PaymentStatus.CANCELLED);
        }

        repository.save(intent);
    }
}
