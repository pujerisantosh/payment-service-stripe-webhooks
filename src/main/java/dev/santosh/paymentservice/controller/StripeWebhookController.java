package dev.santosh.paymentservice.controller;

import com.stripe.model.Event;
import dev.santosh.paymentservice.service.PaymentWebhookService;
import dev.santosh.paymentservice.service.StripeWebhookSignatureVerifier;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/webhooks")
public class StripeWebhookController {

    private final StripeWebhookSignatureVerifier verifier;

    public StripeWebhookController(StripeWebhookSignatureVerifier verifier) {
        this.verifier = verifier;
    }

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            HttpServletRequest request
    ) {
        try {
            // 1️⃣ Read RAW body
            String payload = request.getReader()
                    .lines()
                    .collect(Collectors.joining("\n"));

            // 2️⃣ Get EXACT Stripe header
            String sigHeader = request.getHeader("Stripe-Signature");

            // 3️⃣ Verify signature
            Event event = verifier.verifyAndConstructEvent(payload, sigHeader);

            System.out.println("✅ Stripe event received: " + event.getType());

            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid");
        }
    }
}

