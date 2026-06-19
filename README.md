# RealWorld Spring Boot jOOQ

Spring Boot implementation of the [RealWorld](https://realworld-docs.netlify.app/) API.

This project is a fork of `gothinkster/spring-boot-realworld-example-app` that uses:

- Spring Boot 4.1
- Java 25
- jOOQ for SQL access
- Flyway for schema migration
- H2 file database at `./db/test`
- JWT token authentication
- Spring Boot Actuator health/info endpoints

The API is served under `/api`.

## Requirements

- JDK 25 or newer
- Maven wrapper from this repository
- Optional: [Task](https://taskfile.dev/) for the shortcuts in `Taskfile.yml`
- Optional: Node.js if you want to run the Postman/Newman collection

## Run

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts at `http://localhost:8080/api`.

Useful public endpoints:

- `GET /api/tags`
- `GET /api/articles`
- `GET /api/articles/{slug}`
- `GET /api/articles/{slug}/comments`
- `GET /api/profiles/{username}`
- `GET /api/actuator/health`
- `GET /api/actuator/info`

Authenticated endpoints expect:

```http
Authorization: Token <jwt>
```

## Build And Checks

```bash
./mvnw test
./mvnw -DskipTests compile
./mvnw -q pmd:check
./mvnw -q spring-javaformat:apply
```

With Task installed:

```bash
task compile
task lint
task format
```

## Database And jOOQ

Flyway applies migrations from:

```text
src/main/resources/db/migration
```

jOOQ classes are generated into:

```text
src/main/java/ch/rasc/realworld/db
```

Regenerate them after changing migrations:

```bash
./mvnw -Pjooq generate-sources
```

## API Collection

The `postman` directory contains a Newman runner:

```bash
cd postman
npm install
npm start
```

The collection expects the application to be running at `http://localhost:8080/api`.
