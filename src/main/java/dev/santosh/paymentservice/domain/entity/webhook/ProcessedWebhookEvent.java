package dev.santosh.paymentservice.domain.entity.webhook;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_webhook_events", uniqueConstraints = {@UniqueConstraint(columnNames = "eventId")})
public class ProcessedWebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String eventId;

    private LocalDateTime processedAt;

    protected ProcessedWebhookEvent() {
    }

    public ProcessedWebhookEvent(String eventId) {
        this.eventId = eventId;
        this.processedAt = LocalDateTime.now();
    }

    public String getEventId() {
        return eventId;
    }

}



