# Credit Card Evaluation Service

This project is a **Spring Boot** application for verifying credit card applicants' identity, employment, compliance,
and risk evaluation. The service performs various checks and returns a score indicating the applicant’s
creditworthiness.

## Features

- **Identity Verification**: Verifies Emirates ID and applicant's full name.
- **Employment Verification**: Checks if the applicant is employed and verifies employment details.
- **Compliance Verification**: Ensures the applicant complies with local and international financial regulations.
- **Risk Evaluation**: Evaluates the risk score based on credit limit request and annual income.

## Table of Contents

- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Dependencies](#dependencies)
- [Building and Running](#building-and-running)
- [Postman Collection](#postman-collection)

## Getting Started

### Prerequisites

- **Java 17+**
- **Gradle 7+**
- **Postman** (for testing the API)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MastJagirani/credit-card-evaluation.git
   cd credit-card-evaluation
   ```

2. Ensure that you have Java and Gradle installed on your machine:
   ```bash
   java -version
   gradle -version
   ```

3. Build the application using Gradle:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will run at `http://localhost:8080`.

### Swagger can be accessed at http://localhost:8080/swagger-ui/index.html

## API Endpoints (Postman Collection below)

### 1. Identity Verification

- **URL**: `/api/v1/credit-card/identity-verification`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "emiratesId": "784-1984-1234567-1",
    "fullName": "M Ali"
  }
  ```
- **Response**:
  ```json
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "emiratesId": "784-1984-1234567-1",
    "fullName": "M Ali",
    "identityVerified": true
  }
  ```

  ```json
  {
    "id": null,
    "identityVerified": false
  }
  ```

### 2. Complete Verification

- **URL**: `/api/v1/complete-verification/{id}`
- **Method**: `POST`
- **Request Body**:
  ```json
    {
    "nationality": "ca",
    "mobileNumber": "+971500000000",
    "address": "Al Amal",
    "company": "Microsoft",
    "joinDate": "2024-09-08",
    "employmentType": "FULL_TIME",
    "annualIncome": "2000000",
    "requestedCreditLimit": "2000"
    }
  ```
- **Response**:
  ```json
  { 
    "outcomeType": "STP/NEAR_STP/MANUAL_REVIEW/REJECTED"
  }
  ```

## Dependencies

This application uses the following dependencies:

- Spring Boot Starter Web
- Spring Boot Starter Validation
- Spring Boot Starter Data JPA
- H2 Database (for in-memory database)
- Lombok (for reducing boilerplate code)
- Swagger OpenAPI (for API documentation)

You can find all dependencies in the `build.gradle` file.

## Building and Running

To build the project:

```bash
./gradlew build
```

To run the project:

```bash
./gradlew bootRun
```

To generate the JAR file:

```bash
./gradlew bootJar
```

## Postman Collection

You can test the API using the provided Postman collection. Import the collection into Postman and run the requests.

The Postman collection file is located at:

```
/resources/postman/credit-card-evaluation.postman_collection.json 
```
