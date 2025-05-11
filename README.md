## Currency Exchange App
This Spring Boot application allows users to:
- Add currencies to track
- Fetch exchange rates for a specific currency
- View the list of tracked currencies

Exchange rates are fetched from [openexchangerates.org](https://openexchangerates.org/) **hourly** and stored both in memory and PostgreSQL database.

## REST API Examples
Add currency
POST /api/currencies?code=USD
Content-Type: application/json
{
  "code": "EUR"
}

Get exchange rates for a currency
GET /api/currencies/{code}/rates

Get list of all currencies
GET /api/currencies
---

##  Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Flyway (DB migration)
- Docker (for PostgreSQL)
- Maven
- Springdoc OpenAPI
- JUnit 5, Spring Test

---

## Setup
```bash
# Start PostgreSQL with Docker
docker-compose up -d

# Run app
mvn spring-boot:run
```

### Configuration
Edit `src/main/resources/db.migration/application.yml` and set your OpenExchangeRates App ID.