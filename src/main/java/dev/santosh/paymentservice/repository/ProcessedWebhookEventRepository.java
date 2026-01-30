package dev.santosh.paymentservice.repository;

import dev.santosh.paymentservice.domain.entity.webhook.ProcessedWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedWebhookEventRepository extends JpaRepository<ProcessedWebhookEvent, Long>{

    boolean existsByEventId(String eventId);



}
