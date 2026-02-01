package dev.santosh.paymentservice.service;

import com.stripe.model.Event;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import dev.santosh.paymentservice.domain.entity.PaymentStatus;
import dev.santosh.paymentservice.domain.entity.webhook.ProcessedWebhookEvent;
import dev.santosh.paymentservice.repository.PaymentIntentRepository;
import dev.santosh.paymentservice.repository.ProcessedWebhookEventRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
public class PaymentWebhookService {

    private final PaymentIntentRepository paymentRepository;
    private final ProcessedWebhookEventRepository webhookEventRepository;

    public PaymentWebhookService(
            PaymentIntentRepository paymentRepository,
            ProcessedWebhookEventRepository webhookEventRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.webhookEventRepository = webhookEventRepository;
    }

    @Transactional
    public void handleStripeEvent(Event event) {

        String eventId = event.getId();

        //  Idempotency guard
        if (webhookEventRepository.existsByEventId(eventId)) {
            return;
        }

        try {
            processEvent(event);
        } finally {
            //  Mark webhook as processed (guaranteed)
            webhookEventRepository.save(new ProcessedWebhookEvent(eventId));
        }
    }

    private void processEvent(Event event) {

        String eventType = event.getType();

        if (!eventType.startsWith("payment_intent.")) {
            return;
        }

        Object stripeObject = event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() ->
                        new IllegalStateException("Invalid Stripe payload")
                );

        if (!(stripeObject instanceof com.stripe.model.PaymentIntent stripeIntent)) {
            throw new IllegalStateException("Unexpected Stripe object type");
        }

        String gatewayRef = stripeIntent.getId();

        PaymentIntent payment = paymentRepository
                .findByGatewayReferenceId(gatewayRef)
                .orElseThrow(() ->
                        new IllegalStateException("Payment not found for " + gatewayRef)
                );

        switch (eventType) {

            case "payment_intent.succeeded" -> {
                if (payment.getStatus() == PaymentStatus.CREATED ||
                        payment.getStatus() == PaymentStatus.INITIATED) {
                    payment.setStatus(PaymentStatus.SUCCESS);
                }
            }

            case "payment_intent.payment_failed" -> {
                if (payment.getStatus() != PaymentStatus.SUCCESS) {
                    payment.setStatus(PaymentStatus.FAILED);
                }
            }

            default -> {
                // safely ignored
            }
        }
    }
}
