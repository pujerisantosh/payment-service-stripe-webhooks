package dev.santosh.paymentservice.service;

import dev.santosh.paymentservice.config.AbstractIntegrationTest;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import dev.santosh.paymentservice.repository.PaymentIntentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest
    class PaymentProcessingServiceIT extends AbstractIntegrationTest {

        @Autowired
        PaymentProcessingService paymentService;

        @Autowired
        PaymentIntentRepository repository;

        @Test
        void shouldCreatePaymentAndPersistIntent() {
            PaymentIntent intent =
                    paymentService.createPayment(
                            BigDecimal.valueOf(2500),
                            "INR",
                            "idem-it-001"
                    );

            assertThat(intent.getId()).isNotNull();
            assertThat(intent.getGatewayReferenceId()).isNotBlank();

            PaymentIntent db =
                    repository.findByIdempotencyKey("idem-it-001").orElseThrow();

            assertThat(db.getStatus()).isNotNull();
        }
    }

