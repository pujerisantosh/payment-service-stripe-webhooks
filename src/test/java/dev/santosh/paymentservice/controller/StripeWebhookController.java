package dev.santosh.paymentservice.controller;


import com.stripe.model.Event;
import dev.santosh.paymentservice.service.PaymentWebhookService;
import dev.santosh.paymentservice.service.StripeWebhookSignatureVerifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
    @RequestMapping("/webhooks/stripe")
    public class StripeWebhookController {

        private final StripeWebhookSignatureVerifier signatureVerifier;
        private final PaymentWebhookService webhookService;

        public StripeWebhookController(
                StripeWebhookSignatureVerifier signatureVerifier,
                PaymentWebhookService webhookService
        ) {
            this.signatureVerifier = signatureVerifier;
            this.webhookService = webhookService;
        }

        @PostMapping
        public ResponseEntity<Void> handleWebhook(
                @RequestBody String payload,
                @RequestHeader("Stripe-Signature") String signatureHeader
        ) {
            Event event = signatureVerifier.verifyAndConstructEvent(payload, signatureHeader);

            webhookService.handleStripeEvent(event); // ✅ NOW USED

            return ResponseEntity.ok().build();
        }
    }

