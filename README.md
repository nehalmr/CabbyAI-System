# CabbyAI - Microservices Cab Booking System

A comprehensive cab booking system built with Java 21, Spring Boot microservices architecture, Eureka Discovery Service, and API Gateway.

## Architecture Overview

### Microservices
- **Eureka Server** (Port 8761) - Service Discovery
- **API Gateway** (Port 8080) - Request routing and load balancing
- **User Service** (Port 8081) - User management and authentication
- **Driver Service** (Port 8082) - Driver registration and management
- **Ride Service** (Port 8083) - Ride booking and management
- **Payment Service** (Port 8084) - Payment processing
- **Rating Service** (Port 8085) - Rating and feedback system

### Frontend
- **React Application** (Port 3000) - User interface

## Tech Stack

### Backend
- Java 21
- Spring Boot 3.2.0
- Spring Cloud 2023.0.0
- Spring Cloud Gateway
- Netflix Eureka
- OpenFeign for inter-service communication
- MySQL 8.0 (separate databases per service)
- Swagger/OpenAPI 3
- Maven
- JUnit 5 (TDD approach)

### Frontend
- React 18
- JavaScript (ES6+)
- CSS3 with responsive design
- Fetch API for HTTP requests

## Getting Started

### Prerequisites
- Java 21
- Node.js 16+
- MySQL 8.0
- Maven 3.6+
- Docker & Docker Compose (optional)

### Quick Start with Docker

1. Clone the repository
2. Run with Docker Compose:
   ```bash
   docker-compose up -d
   ```

### Manual Setup

#### 1. Database Setup
```bash
mysql -u root -p < scripts/create-databases.sql
mysql -u root -p < scripts/sample-data-microservices.sql
```

#### 2. Start Services (in order)

1. **Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run
   ```

2. **API Gateway**
   ```bash
   cd api-gateway
   mvn spring-boot:run
   ```

3. **Microservices** (can be started in parallel)
   ```bash
   cd user-service && mvn spring-boot:run &
   cd driver-service && mvn spring-boot:run &
   cd ride-service && mvn spring-boot:run &
   cd payment-service && mvn spring-boot:run &
   cd rating-service && mvn spring-boot:run &
   ```

4. **Frontend**
   ```bash
   cd frontend
   npm install
   npm start
   ```

## Service URLs

- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **Frontend**: http://localhost:3000
- **Individual Service Swagger UIs**:
  - User Service: http://localhost:8081/swagger-ui.html
  - Driver Service: http://localhost:8082/swagger-ui.html
  - Ride Service: http://localhost:8083/swagger-ui.html
  - Payment Service: http://localhost:8084/swagger-ui.html
  - Rating Service: http://localhost:8085/swagger-ui.html

## API Endpoints (via Gateway)

All requests go through the API Gateway at `http://localhost:8080`

### User Management
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login
- `GET /api/users/profile/{userId}` - Get user profile
- `PUT /api/users/profile/{userId}` - Update user profile
- `DELETE /api/users/profile/{userId}` - Deactivate user

### Driver Management
- `POST /api/drivers/register` - Register new driver
- `GET /api/drivers/available` - Get available drivers
- `PUT /api/drivers/status/{driverId}` - Update driver status
- `PUT /api/drivers/location/{driverId}` - Update driver location
- `GET /api/drivers/{driverId}` - Get driver details

### Ride Management
- `POST /api/rides/book` - Book a new ride
- `PUT /api/rides/status/{rideId}` - Update ride status
- `GET /api/rides/user/{userId}` - Get user ride history
- `GET /api/rides/driver/{driverId}` - Get driver ride history
- `POST /api/rides/estimate-fare` - Calculate estimated fare

### Payment Management
- `POST /api/payments/process` - Process payment
- `GET /api/payments/ride/{rideId}` - Get payment by ride
- `GET /api/payments/user/{userId}` - Get user payment history
- `POST /api/payments/refund/{paymentId}` - Refund payment

### Rating Management
- `POST /api/ratings` - Submit rating
- `GET /api/ratings/user/{userId}` - Get user ratings
- `GET /api/ratings/average/{userId}` - Get average rating

## Features

### Core Features
- **Service Discovery**: Automatic service registration and discovery
- **Load Balancing**: Client-side load balancing via Ribbon
- **Circuit Breaker**: Fault tolerance with Resilience4j
- **API Gateway**: Single entry point with routing and filtering
- **Inter-Service Communication**: Feign clients for service-to-service calls
- **Database Per Service**: Each microservice has its own database
- **Health Monitoring**: Actuator endpoints for health checks

### Business Features
- User registration and authentication
- Driver management with real-time location tracking
- Intelligent ride booking with driver assignment
- Automated payment processing
- Rating and feedback system
- Ride history and receipts

## Testing

### Unit Tests
```bash
# Test individual services
cd user-service && mvn test
cd driver-service && mvn test
cd ride-service && mvn test
cd payment-service && mvn test
cd rating-service && mvn test
```

### Integration Tests
```bash
# Test with all services running
mvn verify
```

## Monitoring

- **Eureka Dashboard**: Monitor service registration
- **Actuator Endpoints**: Health checks and metrics
- **Gateway Routes**: View configured routes and filters

## Development Guidelines

### TDD Approach
1. Write failing tests first
2. Implement minimal code to pass tests
3. Refactor while keeping tests green

### Microservices Best Practices
- Single Responsibility per service
- Database per service
- Stateless services
- API versioning
- Centralized configuration
- Distributed tracing (future enhancement)

## Deployment

### Docker Deployment
```bash
docker-compose up -d
```

### Kubernetes Deployment
Kubernetes manifests available in `/k8s` directory (future enhancement)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Implement the feature
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License.

