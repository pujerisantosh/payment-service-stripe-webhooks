package dev.santosh.paymentservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class WebhookSignatureVerifier {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final String webhookSecret;

    public WebhookSignatureVerifier(
            @Value("${payment.webhook.secret}") String webhookSecret
    ) {
        this.webhookSecret = webhookSecret;
    }

    public boolean verify(String payload, String signatureHeader) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    webhookSecret.getBytes(StandardCharsets.UTF_8),
                    HMAC_ALGORITHM
            );
            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getEncoder().encodeToString(rawHmac);

            return expectedSignature.equals(signatureHeader);
        } catch (Exception e) {
            return false;
        }
    }
}
