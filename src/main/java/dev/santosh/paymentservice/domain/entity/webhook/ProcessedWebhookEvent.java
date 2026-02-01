package dev.santosh.paymentservice.domain.entity.webhook;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "processed_webhook_events",
        uniqueConstraints = @UniqueConstraint(columnNames = "event_id")
)
public class ProcessedWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, updatable = false)
    private String eventId;

    protected ProcessedWebhookEvent() {}

    public ProcessedWebhookEvent(String eventId) {
        this.eventId = eventId;
    }
}








