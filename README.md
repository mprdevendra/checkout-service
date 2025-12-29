# checkout-service project description
The Checkout Service is responsible for calculating the final payable amount for a customer’s basket.
It processes basket items, applies applicable promotions, calculates discounts, and returns a detailed price breakdown.

# Assumption:
1. Add item / remove item functionality already exists and is handled by another service or module.
2. This service focuses only on price calculation and promotions.

# Tools & Technologies
Java 21
Spring Boot
Maven
REST APIs
JUnit & Mockito (unit testing)
Intellij Idea
Git

# High-Level Design
The Checkout Service receives a basket request, fetches product and promotion details, calculates line-item prices, applies discounts, and returns the final amount.

Flow:-
1. Receive basket with items and quantities
2. Fetch product prices
3. Fetch applicable promotions
4. Calculate line-item totals
5. Apply discounts
6. Calculate basket total
7. Return response


# Load Balancer & Scalability(Assumed)
1. Service is stateless, enabling horizontal scaling.
2. Behind a load balancer
3. Requests can be distributed across multiple instances.

# Security (Assumed)
Authentication and authorization are assumed to be handled via:
   API Gateway 
   OAuth2 / JWT 

# Layer Architecture
Controller Layer
    ↓
Service Layer
    ↓
Repository Layer

# System Diagram (High-Level)
Client
    ↓
API Gateway
    ↓
Load Balancer
    ↓
Checkout Service
    ↓
+--> Product Service --> Database
|
+--> Promotion Service --> Database

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
            "itemName": "Bananas",
            "quantity": 3
        },
        {
            "itemName": "Oranges",
            "quantity": 4
        },
        {
            "itemName": "Apples",
            "quantity": 1
        }]
   }
```
2. Response body

```md
Item          Quantity    Price
--------------------------------
Bananas       3           1.50
Oranges       4           1.20
Apples        1           0.60
--------------------------------
Subtotal:                  3.30

Discounts:
Buy 2, get 1 free (Bananas) -0.50
3 Oranges for £0.75       -0.45
--------------------------------
Total Discount:          0.95

Total:                   2.35
```









