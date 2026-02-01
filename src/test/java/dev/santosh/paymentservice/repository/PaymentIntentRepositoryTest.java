package dev.santosh.paymentservice.repository;

import dev.santosh.paymentservice.config.AbstractIntegrationTest;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import dev.santosh.paymentservice.domain.entity.PaymentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PaymentIntentRepositoryIT extends AbstractIntegrationTest {

    @Autowired
    private PaymentIntentRepository repository;

    @Test
    void shouldSaveAndFetchPaymentIntent() {
        PaymentIntent intent = new PaymentIntent(
                BigDecimal.valueOf(1000),
                "USD"
        );
        intent.setStatus(PaymentStatus.CREATED);
        intent.setIdempotencyKey("idem-123");

        PaymentIntent saved = repository.save(intent);

        assertThat(saved.getId()).isNotNull();

        PaymentIntent fetched =
                repository.findByIdempotencyKey("idem-123").orElseThrow();

        assertThat(fetched.getAmount()).isEqualByComparingTo("1000");
        assertThat(fetched.getStatus()).isEqualTo(PaymentStatus.CREATED);
    }
}