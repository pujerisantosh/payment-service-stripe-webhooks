💳 Payment Service – Production-Grade Payment Backend

A production-ready payment service built using Spring Boot that demonstrates how real-world payment systems (Stripe / Razorpay / PhonePe style) are designed to handle idempotency, retries, webhooks, and consistency guarantees.


📌 Problem Statement

In real payment systems:

Clients retry requests due to network failures

Payment gateways send duplicate webhooks

Requests must be idempotent

Payments must never be processed twice

State consistency is critical across failures

This service solves these problems by implementing:

Idempotent payment creation

Secure and replay-safe webhook handling

Strong payment state management

Environment parity (local → prod)



🎯 What This Service Does

Creates payments safely even under retries

Integrates with Stripe payment gateway

Verifies webhook authenticity

Prevents duplicate webhook processing

Persists payment lifecycle states reliably



🧠 High-Level Architecture

Client
|
|  (Idempotency-Key)
v
Payment Controller
|
v
Payment Service  ---> Stripe Gateway
|
v
PostgreSQL / H2


🔄 Payment Lifecycle
Each payment follows a strict lifecycle

CREATED → INITIATED → SUCCESS
→ FAILED
→ CANCELLED

Invalid transitions are prevented to ensure data consistency


🔐 Idempotency Design

Every payment request includes an Idempotency-Key

If the same request is retried:

The existing payment is returned

No duplicate payment is created

Prevents double charges


🔔 Webhook Handling (Stripe)

Stripe sends webhooks multiple times to guarantee delivery.

This service ensures:

Webhook signature verification

Webhook event replay protection

Exactly-once processing using DB-backed deduplication

🧪 Tech Stack
Backend

Java 17+

Spring Boot 3.x

Spring Data JPA

Hibernate ORM

Database

H2 (local development)

PostgreSQL (production)

Infrastructure & Tools

Docker & Docker Compose

Stripe Java SDK

Maven

Swagger / OpenAPI


⚙️ Configuration
application.yml (Local – H2)

spring:
application:
name: payment-service

datasource:
url: jdbc:h2:mem:payment_db
driver-class-name: org.h2.Driver
username: sa
password:

jpa:
hibernate:
ddl-auto: create-drop
show-sql: true

h2:
console:
enabled: true
path: /h2-console

payment:
webhook:
secret: whsec_test_dummy


application-prod.yml (PostgreSQL)

spring:
datasource:
url: jdbc:postgresql://localhost:5432/payment_db
username: payment_user
password: payment_pass

jpa:
hibernate:
ddl-auto: validate

payment:
webhook:
secret: ${PAYMENT_WEBHOOK_SECRET}


🐳 Docker Setup (PostgreSQL)
Run PostgreSQL locally using Docker:

docker run -d \
--name payment-postgres \
-e POSTGRES_DB=payment_db \
-e POSTGRES_USER=payment_user \
-e POSTGRES_PASSWORD=payment_pass \
-p 5432:5432 \
postgres:15


Verify:

docker ps


💳 Stripe Setup (Local Development)
1️⃣ Create Stripe Account

Get your test API key from Stripe Dashboard.

2️⃣ Install Stripe CLI

https://stripe.com/docs/stripe-cli

3️⃣ Start Webhook Listener
stripe listen --forward-to localhost:8080/webhooks/stripe

4️⃣ Set Webhook Secret
export PAYMENT_WEBHOOK_SECRET=whsec_xxxxx


(Windows PowerShell)

setx PAYMENT_WEBHOOK_SECRET "whsec_xxxxx"

▶️ Run the Application
Using Maven
./mvnw clean spring-boot:run

Using Profile
./mvnw spring-boot:run -Dspring.profiles.active=prod

📌 API Endpoints
Method	Endpoint	Description
POST	/payments	Create payment (idempotent)
GET	/payments/{id}	Get payment status
POST	/webhooks/stripe	Stripe webhook endpoint

🧪 Testing Strategy

Unit tests for service logic

Integration tests with H2

Idempotency validation tests

Webhook replay simulation

🧠 What This Project Demonstrates

Backend system design (not CRUD)

Payment domain knowledge

Idempotency & retry handling

Secure webhook processing

Production-grade configuration

Dockerized local infrastructure










