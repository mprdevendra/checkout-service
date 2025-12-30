# checkout-service project description
The Checkout Service is responsible for calculating the final payable amount for a customer’s basket.
It processes basket items, applies applicable promotions, calculates discounts, and returns a detailed price breakdown.

# Assumption:
1. Add item / remove item functionality already exists and is handled by another service or module.
2. This service focuses only on price calculation and promotions.

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
8. Return response


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
+--> Product Service --> Database
|
+--> Promotion Service --> Database
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

```json
    {
    "basketId": 1,
    "items": [
      {
        "name": "Bananas",
        "quantity": 3,
        "price": 1.50
      },
      {
        "name": "Oranges",
        "quantity": 4,
        "price": 1.20
      },
      {
        "name": "Apples",
        "quantity": 1,
        "price": 0.60
      }
    ],
    "subTotal": 3.30,
    "discounts": [
    {
      "description": "Buy 2, get 1 free (Bananas)",
      "amount": 0.50
    },
    {
      "description": "3 Oranges for £0.75",
      "amount": 0.45
    }
  ],
    "totalDiscount": 0.95,
    "total": 2.35
  }

```









