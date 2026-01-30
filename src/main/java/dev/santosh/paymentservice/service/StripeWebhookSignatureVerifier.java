package dev.santosh.paymentservice.service;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeWebhookSignatureVerifier {

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public Event verifyAndConstructEvent(String payload, String signatureHeader) {
        try {
            return Webhook.constructEvent(
                    payload,
                    signatureHeader,
                    webhookSecret
            );
        } catch (SignatureVerificationException e) {
            throw new IllegalArgumentException("Invalid Stripe webhook signature", e);
        }
    }
}
