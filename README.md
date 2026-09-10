# AutoNation Customer Lead Management API

Spring Boot 2.7.15 + Java 17 + Spring Data JPA/Hibernate + MySQL 8.0.20 + Lombok.

## Requirements covered

1. Capture customer leads.
2. Manage CRM lifecycle:
   NEW -> ASSIGNED -> INITIAL_CALL -> PROPOSAL -> PRESENT_PROPOSAL -> CLOSED.
3. Support reassignment and lifecycle audit history.
4. Prevent duplicate submissions with business duplicate checks, optional Idempotency-Key, and database uniqueness.
5. Provide a searchable, paginated lead report.
6. Bean Validation and structured error responses.
7. Optimistic locking with `@Version`.
8. SOLID-oriented separation: controllers, services, repositories, mappers, specifications and exceptions.

## Run

Create a MySQL database/user or use the supplied schema.

Example:

```sql
CREATE DATABASE autonation_crm;
```

Update `src/main/resources/application.yml` if required.

Then:

```bash
mvn clean spring-boot:run
```

## APIs

### Create lead

```http
POST /api/leads
Idempotency-Key: 11111111-1111-1111-1111-111111111111
Content-Type: application/json
```

```json
{
  "name": "Srikanth Mamillapalli",
  "email": "smamilla1208@outlook.com",
  "phone": "9739211566",
  "comment": "Interested in a new vehicle"
}
```

### Create account manager

```http
POST /api/account-managers
```

```json
{
  "name": "NG Paul",
  "email": "NgP@outlook.com",
  "phone": "9876500000"
}
```

### Assign

```http
PUT /api/leads/1/assign
```

```json
{
  "accountManagerId": 1,
  "assignedById": 1
}
```

### Lifecycle

```http
PUT /api/leads/1/initial-call
PUT /api/leads/1/proposal
PUT /api/leads/1/present-proposal
```

### Close

```http
PUT /api/leads/1/close
```

```json
{
  "reason": "SOLD",
  "comments": "Customer purchased the vehicle"
}
```

For a lead closed during Initial Call:

```json
{
  "reason": "CANT_MEET_NEED",
  "comments": "Required product is not available"
}
```

After Present Proposal:

```json
{
  "reason": "NO_SALE",
  "comments": "Customer decided not to proceed"
}
```

### Report

```http
GET /api/leads/report
GET /api/leads/report?name=John
GET /api/leads/report?status=PROPOSAL
GET /api/leads/report?accountManagerId=1&status=PROPOSAL
GET /api/leads/report?name=John&email=john@example.com&phone=9876&page=0&size=20
```

### History

```http
GET /api/leads/1/status-history
GET /api/leads/1/assignment-history
```

## Error response

Example:

```json
{
  "timestamp": "2026-09-10T10:30:00",
  "errorCode": "LEAD-001",
  "message": "Customer lead not found: 1001",
  "path": "/api/leads/1001"
}
```

Validation:

```json
{
  "timestamp": "2026-09-10T10:30:00",
  "errorCode": "COMMON-001",
  "message": "Request validation failed",
  "path": "/api/leads",
  "fieldErrors": {
    "name": "Name is required",
    "email": "Email must be valid"
  }
}
```

## Duplicate handling

There are three layers:

- Business duplicate check through service/repository.
- Idempotency-Key for repeated HTTP submissions.
- Database UNIQUE constraint for the idempotency key, protecting against concurrent duplicate inserts.

Email and phone are intentionally not globally unique because a real customer can legitimately create another lead later.

## SOLID structure

- Controllers handle HTTP only.
- Service interfaces define use cases.
- Service implementations contain business rules and transactions.
- Repositories handle persistence.
- Mappers handle DTO/entity conversion.
- Specification isolates dynamic report filtering.
- ErrorCode + Global Exception Handler centralize API error behavior.

## Important Spring Boot 2.7 note

Spring Boot 2.7 uses the `javax.*` namespace, so validation and JPA imports intentionally use:

```java
javax.validation.*
javax.persistence.*
```

Do not change these to `jakarta.*` unless moving to Spring Boot 3.x.
