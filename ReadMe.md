# Vendor Marketplace Microservices Architecture 

---
### 🛒 How This Vendor Marketplace Works

* The backend supports **multiple sellers** and **multiple customers**, managed by **one admin**.
* Each seller can **create an account** and **add products** with a **Maximum Retail Price (MRP)**.
* If a product has a discount, it is **calculated automatically** based on the **MRP and the selling price**.
* When a user places an order:

  * The system creates **separate order records for each seller**, but all are linked to the **same Order ID**.
* After payment:

  * The payment first goes to the **admin’s account**.
  * The admin then **distributes earnings to each seller** using the relevant **Order ID** and **Seller ID**.

 --- 

## Event-Driven Design with SAGA and CQRS Pattern Implementation
[View on Eraser![](https://app.eraser.io/workspace/3HoiqIF3baeAFIL1cqlW/preview)](https://app.eraser.io/workspace/3HoiqIF3baeAFIL1cqlW)

## 🏗️ System Architecture Overview

### **Core Infrastructure Components**

#### **API Gateway**
- **Role**: Single entry point for all client requests
- **Features**:
    - JWT Token validation & authorization
    - Request routing and load balancing
    - Rate limiting and circuit breaking
    - CORS handling and security headers

#### **Service Discovery** - Eureka Netflix
- Dynamic service registration and discovery
- Health monitoring and load distribution
- Service instance management

#### **Configuration Server**
- Centralized configuration management
- Environment-specific properties
- Hot-reload capabilities for configuration changes

#### **Message Broker** - Apache Kafka
- Event-driven communication backbone
- Decouples microservices
- Ensures eventual consistency
- Handles high-volume event processing


The system follows a microservices architecture with Database Per Service pattern, enhanced with event-driven communication and distributed transaction patterns.

## Core Services & Databases

#### **user-service**
- **Database**: PostgreSQL
- Handles user management and profiles

#### **seller-service**
- **Database**: PostgreSQL
- Manages seller accounts and information

#### **product-service** → **CQRS Pattern**
- **Database**: MongoDB
- Implements Command Query Responsibility Segregation pattern for product management
- Handles product catalog and inventory

#### **order-service**
- **Database**: PostgreSQL
- Manages order processing and order lifecycle

#### **payment-service**
- **Database**: PostgreSQL
- Handles payment processing and transactions

#### **cart-service**
- **Database**: Redis
- Manages shopping cart functionality with fast in-memory storage

#### **auth-service**
- **Database**: PostgreSQL + Redis
- **Functionality**: Handles register, login, generate token
- Uses Redis for session management and token storage

#### **coupon-service**
- **Database**: PostgreSQL
- Manages discount coupons and promotional codes

#### **review-wishlist-service**
- **Database**: PostgreSQL
- Handles product reviews and ratings

#### **chatbot-service — In Progress**
- **Database**: MongoDB
- Manages AI chatbot interactions and conversations

#### **seller_report_transaction-service**
- **Database**: PostgreSQL
- Handles seller reporting and transaction analytics

#### **home-service** 
- **Database**: MongoDB
- Manages home page which category shown in landing page

#### **notification-service**
- **Functionality**: Email sending
- Handles all notification delivery including email communications

## Key Architectural Patterns

### **1. Saga Pattern for Order-Payment Transaction**

#### **Problem Statement**
In a distributed microservices environment, traditional ACID transactions across multiple services are not feasible. The order creation and payment processing need to be coordinated across:
- **order-service** (creates order)
- **payment-service** (processes payment)
- **inventory-service** (updates stock)

#### **Saga Implementation - Choreography Pattern**

**Order Creation Saga Flow:**

1. **Order Service** receives order request
2. **Saga Step 1**: Create order with status "PENDING"
3. **Saga Step 2**: Publish `OrderCreatedEvent` to Kafka
4. **Payment Service** consumes event and processes payment
    - If successful: Publish `PaymentSuccessfulEvent`
    - If failed: Publish `PaymentFailedEvent`
5. **Order Service** consumes payment events:
    - On success: Update order status to "CONFIRMED"
    - On failure: Update order status to "FAILED" and initiate compensation

**Compensation Actions:**
- If payment fails: Order service marks order as failed
- If inventory update fails: Payment service initiates refund

#### **Benefits of Saga Pattern:**
- **Eventual Consistency**: Ensures data consistency across services over time
- **Fault Tolerance**: Each step can be retried or compensated
- **Loose Coupling**: Services communicate through events, not direct calls

### **2. Event-Driven Architecture with Kafka**

#### **Why Kafka for E-commerce?**

**User Behavior Analysis:**
- **70% of users** browse products (read-heavy operations)
- **30% of users** actually make purchases (write operations)
- This creates a **read-write disparity** that needs efficient handling

#### **Kafka Implementation Benefits:**

**1. Data Consistency Between Services**
```
Auth Service → UserCreatedEvent → User Service
Auth Service → SellerCreatedEvent → Seller Service
Order Service → OrderCreatedEvent → Payment Service
```

**2. Read-Write Optimization**
- **Write Path**: Fast acknowledgment to users
- **Read Path**: Asynchronous propagation to read models
- **CQRS in product-service**: Separates read and write concerns

**3. Scalability for Browsing Traffic**
- Product catalog reads can be scaled independently
- Event sourcing maintains audit trail
- Real-time inventory updates

#### **Event Flow Examples:**

**User Registration Flow:**
1. Frontend → API Gateway → Auth Service
2. Auth Service creates auth record + publishes `UserCreatedEvent`
3. User Service consumes event → creates user profile
4. Response returned to user immediately (eventual consistency)

**Seller Registration Flow:**
1. Frontend → API Gateway → Auth Service
2. Auth Service creates auth record + publishes `SellerCreatedEvent`
3. Seller Service consumes event → creates seller profile

## Infrastructure Components

### **API Gateway**
- **Token Validation & Authorization**
- Request routing and rate limiting
- Single entry point for all client requests

### **Service Discovery**
- **Eureka Netflix** for service registry
- Dynamic service registration and discovery

### **Configuration Management**
- **Config Server** for centralized configuration
- Environment-specific settings management

### **Security**
- **JWT Token** based authentication
- API Gateway handles token validation
- Role-based access control


## Communication Patterns

### **Synchronous Communication**
- REST APIs between gateway and services
- Used for immediate response requirements

### **Asynchronous Communication (Kafka)**
- Event-driven inter-service communication
- Used for eventual consistency and decoupling
- Handles high-volume read operations efficiently

## Why This Architecture Works for E-commerce

### **Handles Read-Write Disparity**
- 70% browsing (reads) vs 30% purchasing (writes)
- Kafka buffers and distributes load
- CQRS separates read and write models

### **Scalability**
- Each service can scale independently
- Database per service prevents bottlenecks
- Event-driven architecture handles spikes

### **Fault Tolerance**
- Saga pattern ensures transaction consistency
- Event replay capability for recovery
- Circuit breakers prevent cascading failures

### **Development Velocity**
- Teams can work independently on services
- Technology choices per service domain
- Faster deployment cycles

##  Built Through Self-Learning
This enterprise microservices architecture was developed entirely through free resources - **no paid courses or internships**. The skills were acquired from various YouTube channels and official documentation.


###  Want the Learning Roadmap?
For the complete list of YouTube channels and resources that give you enough confidence to build applications like this, connect with me on LinkedIn.

**LinkedIn**: [Muhammad Bilal Khan](https://www.linkedin.com/in/muhammad-bilal-khan-83660931b/)

*Proof that quality education doesn't have to be expensive*
