## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Git
- IntelIj

### Clone the Repository

```bash
git clone  https://github.com/NarmadadeviS/customer-services-springboot.git
cd customer-services-springboot
```

# Customer Services API

This project is a Spring Boot-based REST API that calculates and retrieves reward points earned by customers based on
their purchase history. The reward calculation follows a tiered system and allows querying for reward summaries within a
specific date range.

---

## Features

- Add purchases for a customer along with reward points
- Reward Calculation Logic
  - No rewards for purchases below or equal to $50
  - 1 point per dollar for amounts between $51 and $100
  - 2 points per dollar for amounts above $100
- Fetch reward points by customer ID within a custom date range
- Breakdown of rewards per month
- H2 in-memory database for development
- MySQL support for production
- Input validation and centralized exception handling
- API-level request/response logging
- Unit and integration test

---

## Technology Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- H2 / MySQL
- Mockito
- Maven

---

## Design Details

### Architecture

- **Layered Architecture (Controller → Service → Repository)**
- **Spring Boot** framework using RESTful APIs.
- **JPA** for database access.
- **H2 (Dev)** and **MySQL (Prod)** database configurations using Spring Profiles.

### Data Flow

1. **Add Purchase API**

- Accepts purchase info via DTO.
- Calculates reward points.
- Saves to DB and returns total reward points.

2. **Get Rewards API**

- Accepts customerId and date range.
- Fetches purchase data from DB.
- Aggregates monthly and total reward points.
- Returns a structured response DTO.

### Layer

- **Controller**: Defines API endpoints and input validation.
- **Service**: Contains business logic.
- **Repository**: Handles database operations.
- **DTOs**: Used for clean API contracts.
- **Exception Handling**: Centralized with `@RestControllerAdvice`.
- **Logging**: Request/response logs using filter.

## API Endpoints

### 1. Add Purchase

**POST** `/customer-services/api/v1/add-purchase`

**Request Body:**

```json
{
  "customerId": "CUST123",
  "amount": 100.0
}
```

**Response Body:**

```json
{
  "pointsEarned": 50,
  "totalRewardPoints": 50,
  "timestamp": 1756198551860
}
```

---

### 2. Get Rewards

**Get** `/customer-services/api/v1/get-rewards/CUST123?startDate=2025-08-20&endDate=2025-08-27`

**Response Body:**

```json
{
  "customerId": "CUST123",
  "monthlyRewards": [
    {
      "month": "26-08-2025",
      "points": 50
    }
  ],
  "totalRewards": 50,
  "timestamp": 1756198457347
}
```

---

## Note:

- Postman API collection and environment json is available inside resources folder.
- server port as 8089
