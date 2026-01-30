package dev.santosh.paymentservice.controller;

import dev.santosh.paymentservice.controller.dto.CreatePaymentRequest;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import dev.santosh.paymentservice.service.PaymentProcessingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentProcessingService paymentProcessingService;

    public PaymentController(PaymentProcessingService paymentProcessingService) {
        this.paymentProcessingService = paymentProcessingService;
    }

    @PostMapping("/payments")
    public PaymentIntent createPayment(
            @RequestBody CreatePaymentRequest request,
            @RequestHeader("Idempotency-Key") String key
    ) {
        return paymentProcessingService.createPayment(
                request.getAmount(),
                request.getCurrency(),
                key
        );
    }





}
