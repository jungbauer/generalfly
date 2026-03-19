# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Generalfly is a Spring Boot web application deployed to Fly.io. It has two main functional areas:
1. **Comics Management** - A personal comic book tracking system with CRUD operations, import/export via JSON
2. **NHL API Proxy** - REST endpoints that proxy/cache NHL API data for a GitHub Pages static site

## Build Commands

```bash
# Build the application
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "CacheHeaderTests"

# Clean build artifacts
./gradlew clean

# Run locally (requires local PostgreSQL)
./gradlew bootRun

# Build Docker image for deployment
./build-image.sh
```

## Development Environment

### Database Setup
- PostgreSQL 17 runs via Docker Compose: `docker compose up`
- Local DB connection: `jdbc:postgresql://localhost:5432/generalfly`
- Default credentials in `application.properties`: user `generalfly`, password `localsecret`
- Required schemas: `comics`, `nhl` (created manually, see README.md)

### Profiles
- **dev** (default): Local development with localhost PostgreSQL
- **prod**: Production on Fly.io (uses env vars for DB credentials)
- **dockerdev**: Running locally in Docker container with host networking

## Architecture

### Security Model (`WebSecurityConfig.java`)
- Two security filter chains using `@Order`:
  1. **Admin chain** (`@Order(1)`): `/actuator/**` and `/user/**` require ROLE_ADMIN
  2. **Default chain**: Public paths include `/`, `/login`, `/logout`, `/error`, `/nhl/**`; all others require authentication
- Form-based login with BCrypt password encoding
- CORS configured for `/nhl/**` endpoints to allow GitHub Pages origin

### Domain Structure
- **comics** schema: `Comic` entity tied to authenticated users; users can only access their own comics
- **nhl schema**: `Team`, `Game`, `Conference`, `Division` - populated from external NHL API
- **default schema**: `User`, `Role`, `DumpLog` for authentication and request logging

### NHL Data Flow
1. `NhlController` exposes REST endpoints with 12-hour HTTP cache headers
2. `NhlApiService` uses Spring's `RestClient` to call `api-web.nhle.com/v1`
3. `NhlDataService` handles bulk data collection and database persistence
4. DTOs in `dto.nhl.api` package map directly to NHL API JSON structure

### Async Logging
- `DumpLogService` uses `@Async` to log API requests to the database without blocking responses
- Called from `NhlController` endpoints to track usage with IP addresses

### Thymeleaf Layout
- `layout.html` provides base template with Bootstrap 5, navigation, and security-aware menu items
- Content pages use `layout:fragment="content"` and `layout:decorate="~{layout}"`
- `sec:authorize` attributes control UI based on roles

## Coding Standards

### Lombok
- Use Lombok annotations (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`) for DTOs and entities to minimize boilerplate
- Follow existing patterns in the codebase (see `dto.nhl.api` package for examples)
- Custom constructors can still be added when mapping from entities is needed

## Testing

- Uses JUnit 5 with Spring Boot Test
- `@AutoConfigureMockMvc` for controller tests
- `@MockitoBean` to mock external API service
- `@WithMockUser` for security context in tests

## Deployment

- Fly.io configuration in `fly.toml`
- Docker image built locally and deployed: `fly deploy --ha=false --local-only --image generalfly:latest`
- Database migrations handled by Hibernate `ddl-auto=update` (not Flyway)
