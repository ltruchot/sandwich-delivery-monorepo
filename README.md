# Sandwich Delivery Monorepo

A simple sandwich delivery system with Spring Boot API and Astro frontend.

## Project Structure

```
sandwich-delivery-monorepo/
   apps/
      spring-boot-api/         # Java 21 Spring Boot API
      astro-frontend/          # Astro frontend with Tailwind CSS 4.1
   packages/
      tailwind-preset/         # Shared Tailwind configuration
   docker-compose.yml           # PostgreSQL database
```

## Prerequisites

- Java 21
- Node.js 18+
- PNPM
- Docker & Docker Compose

## Getting Started

### 1. Start the Database

```bash
docker-compose up -d
```

Note: PostgreSQL runs on port 5433 locally to avoid conflicts.

### 2. Install Frontend Dependencies

```bash
pnpm install
```

### 3. Run the Backend

```bash
cd apps/spring-boot-api
./gradlew bootRun
```

The API will be available at http://localhost:8080 with the `local` profile.

### 4. Run the Frontend

In a new terminal:

```bash
pnpm dev:frontend
```

The frontend will be available at http://localhost:4321

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login

### Orders
- `GET /api/orders/basket` - Get current basket
- `POST /api/orders/basket/sandwich` - Add sandwich to basket
- `POST /api/orders/pay` - Pay for order
- `GET /api/orders` - Get user orders

### Toppings
- `GET /api/toppings` - Get all available toppings

## Features

- **Sandwiches**: Ham or Vegetarian (5� each)
- **Toppings**: Salad, Tomato, Onion, Cheese (1� each)
- **Limits**: Max 4 different toppings, max 2 of each type
- **Simple Auth**: Email/password with JWT (no recovery)
- **Orders**: One basket at a time, fake payment always works

## Configuration Profiles

The Spring Boot API uses profiles for different environments:

- **local** (default): Uses PostgreSQL on port 5433
- **production**: Uses environment variables for Render.com deployment

### Environment Variables for Production

- `DATABASE_URL`: PostgreSQL connection string (provided by Render)
- `JWT_SECRET`: Secret key for JWT tokens
- `CORS_ALLOWED_ORIGINS`: Comma-separated list of allowed origins
- `SPRING_PROFILES_ACTIVE`: Set to `production` on Render

## Deployment to Render.com

1. Fork/push this repo to GitHub
2. Create a new Web Service on Render
3. Connect your GitHub repo
4. Set root directory to `apps/spring-boot-api`
5. Render will automatically:
   - Create PostgreSQL database (using render.yaml)
   - Set environment variables
   - Build and deploy using Dockerfile

For the frontend:
1. Create a Static Site on Render
2. Set root directory to `apps/astro-frontend`
3. Build command: `pnpm install && pnpm build`
4. Publish directory: `dist`

## Development Notes

- The monorepo uses PNPM workspaces
- Tailwind CSS 4.1 configuration is shared via preset
- Database schema is auto-created via JPA
- Local uses port 5433 for PostgreSQL to avoid conflicts
- Production configuration is managed via environment variables