package dev.santosh.paymentservice.gateway;

import com.stripe.Stripe;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import dev.santosh.paymentservice.domain.entity.PaymentIntent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("stripe")
public class StripePaymentGateway implements PaymentGateway {

    public StripePaymentGateway() {
        Stripe.apiKey = "sk_test_dummy"; // will move to config later
    }

    @Override
    public String initiate(PaymentIntent paymentIntent) {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(paymentIntent.getAmount().longValue())
                        .setCurrency(paymentIntent.getCurrency().toLowerCase())
                        .build();

        try {
            com.stripe.model.PaymentIntent stripeIntent =
                    com.stripe.model.PaymentIntent.create(
                            params,
                            RequestOptions.builder().build()
                    );

            return stripeIntent.getId();

        } catch (Exception e) {
            throw new RuntimeException("Stripe payment initiation failed", e);
        }
    }

    @Override
    public String initiate(BigDecimal amount, String currency, String idempotencyKey) {
        return "";
    }
}
