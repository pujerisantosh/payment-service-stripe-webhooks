package dev.santosh.paymentservice.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentWebhookRequest {
    private String eventId;
    private String eventType;
    private String gatewayReferenceId;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
