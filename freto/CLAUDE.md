# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Freto** is a logistics platform (similar to Uber for freight/moving) that connects clients needing transport with partner drivers. The core architecture is event-driven microservices with asynchronous matching via RabbitMQ.

## Repository Structure

```
freto-backend/usuario-service/   # Spring Boot microservice for user management
freto-frontend/                  # React + TypeScript SPA
freto-docs/                      # Architecture diagrams, requirements, test plans
docker-compose.yml               # Orchestrates: frontend, usuario-service, rabbitmq
```

## Commands

### Frontend (`freto-frontend/`)
```bash
npm install          # install dependencies
npm run dev          # dev server on localhost:5173
npm run build        # tsc + vite build (output to dist/)
npm run lint         # eslint
npm run preview      # preview production build
```

### Backend — usuario-service (`freto-backend/usuario-service/`)
```bash
./mvnw spring-boot:run           # run locally (requires env vars)
./mvnw package -DskipTests       # build JAR
./mvnw test                      # run all tests
./mvnw test -Dtest=ClassName     # run a single test class
```

### Docker (full stack)
```bash
docker compose up --build        # build and start all services
docker compose up -d             # start detached
docker compose down              # stop all services
```

### Ports
| Service       | Port  |
|---------------|-------|
| Frontend      | 3000  |
| usuario-service | 8081 |
| RabbitMQ AMQP | 5672  |
| RabbitMQ UI   | 15672 |

## Environment Variables

Copy `.env.example` to `.env` (or set directly). Required vars:
- `DB_URL` — Azure SQL Database JDBC connection string
- `DB_USER` / `DB_PASSWORD` — database credentials
- `SPRING_RABBITMQ_USERNAME` / `SPRING_RABBITMQ_PASSWORD` — RabbitMQ credentials

## Architecture

### Backend
- **Microservices**: each service lives under `freto-backend/<service-name>/` with its own `pom.xml`, `Dockerfile`, and Spring Boot application.
- **Current service**: `usuario-service` — user CRUD, Spring Boot 4.x, Spring Data JPA (not JDBC), Spring Security, connects to Azure SQL Database via `mssql-jdbc`.
- **Package structure**: `com.freto.usuarioService.{controller,service,repository,model}`
- **Async messaging** (planned): RabbitMQ via Spring AMQP for client–driver matching.

### Frontend
- **Framework**: React 19 + TypeScript, Vite 8, React Router v7 (basename `/freto/`).
- **Styling**: Bootstrap 5 + react-bootstrap + Tailwind CSS v4 (via `@tailwindcss/vite` plugin).
- **State**: Zustand.
- **HTTP**: Axios.
- **WebSocket**: STOMP over SockJS (`@stomp/stompjs` + `sockjs-client`).
- **Current pages**: Login, Register, ForgotPassword, Success — all wrapped in `AuthLayout`.
- **Routing**: defined in `src/main.tsx`; root redirects to `/login`.

### Design reference
Figma layout: `https://www.figma.com/design/yH9mLS1YyXnxLg7HYd14Lq/Design-Freto`
