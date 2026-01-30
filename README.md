💳 Payment Service – Stripe Webhooks (Production-Grade)

A production-ready Payment Service built using Spring Boot that integrates with Stripe Payment Intents and secure webhook processing.
This service demonstrates real-world payment flows, idempotency, event-driven state transitions, and secure webhook signature verification — all essential for SDE-2 backend roles.

🚀 Key Highlights

✅ Stripe Payment Intent integration

🔐 Secure Webhook Signature Verification

♻️ Idempotent payment creation

🔄 Event-driven payment state transitions

🧱 Clean layered architecture

🧪 Local testing using Stripe CLI

🗄️ JPA + H2 (pluggable with PostgreSQL)

📈 Designed for production extensibility

🧠 Why This Project?

Payments are stateful, asynchronous, and failure-prone.
This project models how real payment systems are built in production, not just API demos.

It covers:

Async webhook delivery

Duplicate event handling

Secure event verification

Domain-driven payment lifecycle



🏗️ High-Level Architecture

Client
  |
  |  REST API
  v
PaymentController
  |
PaymentProcessingService
  |
PaymentGateway (Stripe)
  |
Stripe API  ───────────▶  Stripe Webhooks
                             |
                             v
                    StripeWebhookController
                             |
               StripeWebhookSignatureVerifier
                             |
                      PaymentIntentRepository
                             |
                           Database


📦 Tech Stack


| Category    | Technology                        |
| ----------- | --------------------------------- |
| Language    | Java 17+                          |
| Framework   | Spring Boot 3                     |
| Payments    | Stripe Java SDK                   |
| Persistence | Spring Data JPA                   |
| Database    | H2 (dev), PostgreSQL (prod-ready) |
| Webhooks    | Stripe CLI                        |
| Build Tool  | Maven                             |


📁 Project Structure

src/main/java/dev/santosh/paymentservice
├── controller
│   ├── PaymentController
│   └── StripeWebhookController
│
├── service
│   ├── PaymentProcessingService
│   ├── StripeWebhookSignatureVerifier
│   └── PaymentStateTransitionValidator
│
├── gateway
│   ├── PaymentGateway
│   └── StripePaymentGateway
│
├── domain
│   └── entity
│       ├── PaymentIntent
│       └── PaymentStatus
│
├── repository
│   └── PaymentIntentRepository
│
└── PaymentServiceApplication


🔁 Payment Lifecycle

CREATED
  ↓
INITIATED
  ↓ (payment_intent.succeeded)
SUCCESS
  ↓
FAILED / CANCELLED (on failure events)

All state transitions are:

validated

idempotent

webhook-driven


🔐 Webhook Security


Stripe signs every webhook event

Signature is verified using:

raw request body

Stripe-Signature header

webhook secret

Webhook.constructEvent(payload, signatureHeader, webhookSecret);
Invalid or tampered requests are rejected immediately.



♻️ Idempotency Handling

Each payment request contains an idempotency key

Prevents:

duplicate charges

webhook replays

Stripe + DB enforce uniqueness




⚙️ Getting Started (Local Setup)
Prerequisites

Java 17+

Maven

Stripe CLI

Stripe test account

1️⃣ Clone Repository
git clone https://github.com/pujerisantosh/payment-service-stripe-webhooks.git
cd payment-service-stripe-webhooks

2️⃣ Configure Application

application.properties

spring.application.name=payment-service

stripe.api.key=sk_test_XXXXXXXXXXXXXXXX
stripe.webhook.secret=whsec_XXXXXXXXXXXXXXXX

spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=update

3️⃣ Run Application
mvn clean install
mvn spring-boot:run


Application runs at:

http://localhost:8080

🧪 Webhook Testing with Stripe CLI
Start webhook listener
stripe listen --forward-to http://127.0.0.1:8080/webhooks/stripe


Copy the generated whsec_XXXX and update config.

Trigger Events
stripe trigger payment_intent.succeeded


Expected result:

HTTP 200 from webhook

Payment status updated in DB

Events visible in Stripe Dashboard

📊 Verify Results
Stripe Dashboard

Developers → Events

Developers → Logs

Payments → Payment Intents

Local DB

H2 Console: /h2-console

Inspect payment_intent table

🧩 API Example
Create Payment
curl -X POST http://localhost:8080/payments \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 1000,
    "currency": "usd",
    "idempotencyKey": "order-123"
  }'

🧪 Testing Strategy (Planned)

Unit tests for:

Payment state transitions

Webhook signature verification

Integration tests using:

H2

Stripe CLI

Contract tests for webhooks

🚀 Production Readiness

This project is designed to scale with:

PostgreSQL

Redis (idempotency + caching)

Kafka / SQS (event propagation)

Retry + DLQ support

Multi-gateway support (Razorpay, Adyen)





👤 Author

Santosh Pujeri
Backend / Platform Engineering
GitHub: https://github.com/pujerisantosh
