💳 Payment Service – Stripe Integration (Spring Boot)


Overview


This project implements a production-style payment service using Spring Boot and Stripe PaymentIntents, designed to handle real-world payment flows including asynchronous confirmation via webhooks, idempotency, and secure signature verification.

The system follows clean architecture principles and is extensible to support multiple payment gateways in the future.

🚀 Key Features

Stripe PaymentIntent integration

Secure webhook handling with Stripe signature verification

Asynchronous payment status updates

Idempotent payment creation

Gateway abstraction layer (Stripe is one implementation)

Persistent payment state management

Local webhook testing using Stripe CLI

H2 in-memory DB for local development (PostgreSQL-ready)

🏗 High-Level Architecture
Client
  |
  v
Payment API (Spring Boot)
  |
  v
Stripe API (PaymentIntent)
  |
  v
Stripe Webhooks
  |
  v
Webhook Controller
  |
  v
Payment Processing Service
  |
  v
Database

🧠 Design Decisions
Why Webhooks?

Stripe payments are asynchronous. A successful API call does not guarantee payment completion.
Webhooks are treated as the source of truth for final payment status.

Why Idempotency?

To safely handle retries and prevent duplicate payments, idempotency keys are used during payment creation.

Why Gateway Abstraction?

The PaymentGateway interface allows:

Easy extension to other providers (Razorpay, PayPal, etc.)

Clean separation between business logic and external integrations

🗂 Package Structure
controller/
 ├── StripeWebhookController
 ├── PaymentController

service/
 ├── PaymentProcessingService
 ├── StripeWebhookSignatureVerifier

gateway/
 ├── PaymentGateway
 ├── StripePaymentGateway

domain/
 ├── entity/
 │    ├── PaymentIntent
 │    ├── PaymentStatus
 └── dto/

repository/
 ├── PaymentIntentRepository

⚙️ Configuration
application.properties
spring.application.name=payment-service

# Stripe configuration
stripe.api.key=YOUR_STRIPE_SECRET_KEY
stripe.webhook.secret=YOUR_WEBHOOK_SECRET

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update


⚠️ Never commit real Stripe secrets to source control.

🧪 Local Testing with Stripe CLI
1. Login to Stripe CLI
stripe login

2. Forward webhooks to local app
stripe listen --forward-to http://127.0.0.1:8080/webhooks/stripe

3. Trigger test events
stripe trigger payment_intent.succeeded


You should see:

Events in Stripe CLI

HTTP 200 responses from your application

Payment status updates in the database

📊 Verification & Monitoring
Stripe Dashboard

Developers → Events → View webhook events

Developers → Logs → Verify API requests

Payments → PaymentIntents → Confirm lifecycle

Application Logs

Webhook signature verification

Event type handling

Payment state transitions

🔐 Security Considerations

Stripe webhook signatures are verified using the official Stripe SDK

Secrets are externalized via configuration

Invalid or tampered webhook requests are rejected

🔮 Future Enhancements

Retry handling & dead-letter queue

Event de-duplication using Stripe event IDs

PostgreSQL integration for production

Distributed locking for concurrent webhook delivery

Support for multiple payment gateways

Metrics & alerting (Micrometer + Prometheus)

🎯 Why This Project Matters

This project demonstrates:

Real-world backend payment design

Asynchronous system handling

Third-party integration best practices

Production-ready error handling and security

It reflects SDE-2 level backend engineering skills with a focus on correctness, reliability, and extensibility.

👨‍💻 Author

Santosh Pujeri Backend Developer | Payment Integration Specialist 📧 pujersantosh.backend@gmail.com

📞 +91 7338110806
