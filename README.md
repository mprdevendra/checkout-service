# checkout-service project description
The Checkout Service is responsible for calculating the final payable amount for a customer’s basket.
It processes basket items, applies applicable promotions, calculates discounts, and returns a detailed price breakdown.
This service focuses only on price calculation and promotions.

# Assumption:
1. Add item / remove item functionality already exists and is handled by another service or module.

# Tools & Technologies
```md
Java 8
Spring Boot
Maven
REST APIs
JUnit & Mockito (unit testing)
Intellij Idea
Git
```

# High-Level Design
The Checkout Service receives a basket request, fetches product and promotion details, calculates line-item prices, applies discounts, and returns the final amount.

Flow:-
1. Receive basket with items and quantities
2. Validation on request basket and basketItems
3. Fetch product prices
4. Fetch applicable promotions
5. Calculate line-item totals
6. Apply discounts
7. Calculate basket total
8. Generate receipt response
9. return response
10. If exception then ApplicationExceptionHandler would handle.

# Load Balancer & Scalability(Assumed)
1. Service is stateless, enabling horizontal scaling.
2. Behind a load balancer
3. Requests can be distributed across multiple instances.

# Security (Assumed)
```md
Authentication and authorization are assumed to be handled via:
   API Gateway 
   JWT
```
# Global Exception Handler (Implemented)
If exception then ApplicationExceptionHandler would handle.

# Input validation(Implemented)
Input request on basket level validation implemented.

# Layer Architecture
```md
Controller Layer
    ↓
Service Layer
    ↓
Repository Layer
```

# System Diagram (High-Level)
```md
Client
    ↓
API Gateway
    ↓
Load Balancer
    ↓
Checkout Service
    ↓
+--> Price Service 
           |
           +Product service --> Database
|
+--> Promotion Service --> database
           |
           + Promotion Registry
           |
           + Promotion strategy
|
+--> Calculation service
|
+--> Receipt Service

if Exception-
|
+--> Application Exception Handler

```
## How to Run the application

```bash
mvn clean install
mvn spring-boot:run
```

# Server run on
Server runs on:
```
http://localhost:9001
```

# EndPoint
Calculate the basket price
http://localhost:9001/api/v1/checkout/basket/calculate

1.Request body

```json
   {
    "basketId" : 1,
    "basketItems": [
      {
        "itemCode": "BANANAS",
        "quantity": 3
      },
      {
        "itemCode": "ORANGES",
        "quantity": 4
      },
      {
        "itemCode": "APPLES",
        "quantity": 1
      }]
    }
```
2. Response body

```json
    {
      "basketId": 1,
      "items": [
        {
          "itemCode": "BANANAS",
          "quantity": 3,
          "price": 1.50
        },
        {
          "itemCode": "ORANGES",
          "quantity": 4,
          "price": 1.20
        },
        {
          "itemCode": "APPLES",
          "quantity": 1,
          "price": 0.60
        }],
      "subTotal": 3.30,
      "discounts": [
        {
          "description": "Buy 2, get 1 free (BANANAS)",
          "amount": 0.50
        },
        {
          "description": "3 ORANGES for £0.75",
          "amount": 0.45
        }],
      "totalDiscount": 0.95,
      "total": 2.35
    }

```
# Not Implemented
1. Price versioning
2. Discount versioning
3. Category/Brand/Cart Level promotion









