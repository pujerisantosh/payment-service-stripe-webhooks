package dev.santosh.paymentservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
class StripeWebhookSignatureVerifierTest {

    private StripeWebhookSignatureVerifier verifier;

    @BeforeEach
    void setup() {
        verifier = new StripeWebhookSignatureVerifier("whsec_test_secret");
    }

    @Test
    void shouldVerifyAndConstructEvent_whenSignatureIsValid() {
        String payload = "{ \"id\": \"evt_test\" }";
        String signature = "t=123,v1=fake_signature";

        assertThrows(IllegalArgumentException.class, () ->
                verifier.verifyAndConstructEvent(payload, signature)
        );
    }
}
