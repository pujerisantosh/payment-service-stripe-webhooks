package dev.santosh.paymentservice.repository;

import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentIntentRepository extends JpaRepository<PaymentIntent, Long> {

    Optional<PaymentIntent> findByGatewayReferenceId(String gatewayReferenceId);


    Optional<PaymentIntent> findByIdempotencyKey(String idempotencyKey);
}
