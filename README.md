# CabbyA I - Production-Level Microservices Architecture

A comprehensive, production-ready cab booking system built with Java 21, Spring Boot microservices, and modern cloud-native patterns.

## 🏗️ Architecture Overview

### Microservices
- **Config Server** (Port 8888) - Centralized configuration management
- **Eureka Server** (Port 8761) - Service discovery and registration
- **Security Service** (Port 8086) - JWT token management and authentication
- **API Gateway** (Port 8080) - Single entry point, routing, and security
- **User Service** (Port 8081) - User management and authentication
- **Driver Service** (Port 8082) - Driver registration and management
- **Ride Service** (Port 8083) - Ride booking and lifecycle management
- **Payment Service** (Port 8084) - Payment processing and transactions
- **Rating Service** (Port 8085) - Rating and feedback system
- **Notification Service** (Port 8087) - Real-time notifications

### Frontend
- **React Application** (Port 3000) - Modern SPA with JWT authentication

## 🚀 Key Features

### Production-Ready Features
- **Microservices Architecture**: Fully decoupled services with independent databases
- **Service Discovery**: Automatic service registration with Eureka
- **Centralized Configuration**: Spring Cloud Config for environment-specific settings
- **API Gateway**: Single entry point with routing, load balancing, and security
- **JWT Authentication**: Stateless, secure token-based authentication
- **Circuit Breakers**: Fault tolerance with Resilience4j
- **Health Monitoring**: Comprehensive health checks and metrics
- **Distributed Logging**: Structured logging across all services
- **Database Per Service**: Complete data isolation between services

### Security Features
- **JWT Token Management**: Secure, stateless authentication
- **Password Encryption**: BCrypt hashing with salt
- **Input Validation**: Comprehensive validation with Bean Validation
- **SQL Injection Prevention**: JPA/Hibernate parameterized queries
- **CORS Configuration**: Secure cross-origin resource sharing

### Monitoring & Observability
- **Health Endpoints**: `/actuator/health` on all services
- **Metrics**: Prometheus metrics integration
- **Service Discovery Dashboard**: Eureka dashboard for service monitoring
- **Structured Logging**: JSON-formatted logs with correlation IDs

## 🛠️ Tech Stack

### Backend
- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.2.0** - Latest Spring Boot with native compilation support
- **Spring Cloud 2023.0.0** - Cloud-native patterns and tools
- **Spring Security 6.x** - Comprehensive security framework
- **Spring Cloud Gateway** - Reactive API gateway
- **Netflix Eureka** - Service discovery
- **Spring Cloud Config** - Centralized configuration
- **OpenFeign** - Declarative REST clients
- **Resilience4j** - Circuit breakers and fault tolerance
- **MySQL 8.0** - Production-grade database
- **JWT** - Stateless authentication
- **Bean Validation** - Input validation
- **Swagger/OpenAPI 3** - API documentation
- **Maven** - Dependency management
- **Docker** - Containerization

### Frontend
- **React 18** - Modern UI library
- **JavaScript ES6+** - Modern JavaScript features
- **CSS3** - Responsive design
- **JWT Integration** - Secure authentication

## 📋 Prerequisites

- Java 21
- Node.js 18+
- MySQL 8.0
- Maven 3.8+
- Docker & Docker Compose

## 🚀 Quick Start

### Using Docker Compose (Recommended)

1. **Clone the repository**
   \`\`\`bash
   git clone https://github.com/your-org/cabbyai.git
   cd cabbyai
   \`\`\`

2. **Set environment variables**
   \`\`\`bash
   cp .env.example .env
   # Edit .env with your configuration
   \`\`\`

3. **Start all services**
   \`\`\`bash
   docker-compose up -d
   \`\`\`

4. **Verify services are running**
   \`\`\`bash
   docker-compose ps
   \`\`\`

### Manual Setup

1. **Start MySQL**
   \`\`\`bash
   mysql -u root -p < scripts/create-databases.sql
   \`\`\`

2. **Start services in order**
   \`\`\`bash
   # 1. Config Server
   cd config-server && mvn spring-boot:run &
   
   # 2. Eureka Server
   cd eureka-server && mvn spring-boot:run &
   
   # 3. Security Service
   cd security-service && mvn spring-boot:run &
   
   # 4. API Gateway
   cd api-gateway && mvn spring-boot:run &
   
   # 5. Business Services (parallel)
   cd user-service && mvn spring-boot:run &
   cd driver-service && mvn spring-boot:run &
   cd ride-service && mvn spring-boot:run &
   cd payment-service && mvn spring-boot:run &
   cd rating-service && mvn spring-boot:run &
   cd notification-service && mvn spring-boot:run &
   \`\`\`

3. **Start Frontend**
   \`\`\`bash
   cd frontend
   npm install
   npm start
   \`\`\`

## 🔗 Service URLs

- **Frontend**: http://localhost:3000
- **API Gateway**: http://localhost:8080
- **Eureka Dashboard**: http://localhost:8761
- **Config Server**: http://localhost:8888
- **Individual Services**:
  - Security Service: http://localhost:8086/swagger-ui.html
  - User Service: http://localhost:8081/swagger-ui.html
  - Driver Service: http://localhost:8082/swagger-ui.html
  - Ride Service: http://localhost:8083/swagger-ui.html
  - Payment Service: http://localhost:8084/swagger-ui.html
  - Rating Service: http://localhost:8085/swagger-ui.html
  - Notification Service: http://localhost:8087/swagger-ui.html

## 🔐 Authentication

All API requests (except registration and login) require JWT authentication:

\`\`\`bash
# Login to get JWT token
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "password": "password123"}'

# Use token in subsequent requests
curl -X GET http://localhost:8080/api/users/profile/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
\`\`\`

## 📊 Monitoring

### Health Checks
\`\`\`bash
# Check all services health
curl http://localhost:8080/actuator/health

# Check individual service health
curl http://localhost:8081/actuator/health
\`\`\`

### Metrics
\`\`\`bash
# Prometheus metrics
curl http://localhost:8080/actuator/prometheus
\`\`\`

### Service Discovery
- Visit http://localhost:8761 to see registered services

## 🧪 Testing

### Unit Tests
\`\`\`bash
# Test all services
./test-all.sh

# Test individual service
cd user-service && mvn test
\`\`\`

### Integration Tests
\`\`\`bash
# Run integration tests
mvn verify -Pintegration-tests
\`\`\`

### Load Testing
\`\`\`bash
# Using Apache Bench
ab -n 1000 -c 10 http://localhost:8080/api/users/profile/1
\`\`\`

## 📈 Performance

### Database Optimization
- Connection pooling with HikariCP
- Proper indexing on frequently queried columns
- Query optimization with JPA/Hibernate

### Caching
- Application-level caching with Spring Cache
- Database query result caching

### Load Balancing
- Client-side load balancing with Ribbon
- Multiple service instances support

## 🔧 Configuration

### Environment Variables
\`\`\`bash
# Database
MYSQL_ROOT_PASSWORD=your_password
MYSQL_HOST=localhost
MYSQL_PORT=3306

# JWT
JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000

# Service Discovery
EUREKA_SERVER_URL=http://localhost:8761/eureka/

# Config Server
CONFIG_SERVER_URL=http://localhost:8888
\`\`\`

### Application Profiles
- `dev` - Development environment
- `test` - Testing environment
- `prod` - Production environment

## 🚀 Deployment

### Docker Deployment
\`\`\`bash
# Build all images
docker-compose build

# Deploy to production
docker-compose -f docker-compose.prod.yml up -d
\`\`\`

### Kubernetes Deployment
\`\`\`bash
# Apply Kubernetes manifests
kubectl apply -f k8s/
\`\`\`

### Cloud Deployment
- AWS ECS/EKS
- Google Cloud Run/GKE
- Azure Container Instances/AKS

## 📝 API Documentation

### Swagger UI
Each service provides interactive API documentation:
- User Service: http://localhost:8081/swagger-ui.html
- Driver Service: http://localhost:8082/swagger-ui.html
- Ride Service: http://localhost:8083/swagger-ui.html
- Payment Service: http://localhost:8084/swagger-ui.html
- Rating Service: http://localhost:8085/swagger-ui.html

### API Endpoints

#### User Management
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login
- `GET /api/users/profile/{userId}` - Get user profile
- `PUT /api/users/profile/{userId}` - Update user profile

#### Driver Management
- `POST /api/drivers/register` - Register new driver
- `GET /api/drivers/available` - Get available drivers
- `PUT /api/drivers/status/{driverId}` - Update driver status
- `PUT /api/drivers/location/{driverId}` - Update driver location

#### Ride Management
- `POST /api/rides/book` - Book a new ride
- `PUT /api/rides/status/{rideId}` - Update ride status
- `GET /api/rides/user/{userId}` - Get user ride history
- `POST /api/rides/estimate-fare` - Calculate estimated fare

#### Payment Management
- `POST /api/payments/process` - Process payment
- `GET /api/payments/ride/{rideId}` - Get payment by ride
- `GET /api/payments/user/{userId}` - Get user payment history

#### Rating Management
- `POST /api/ratings` - Submit rating
- `GET /api/ratings/user/{userId}` - Get user ratings
- `GET /api/ratings/average/{userId}` - Get average rating

## 🔒 Security Best Practices

1. **JWT Tokens**: Short-lived tokens (24 hours)
2. **Password Security**: BCrypt hashing with salt
3. **Input Validation**: Comprehensive validation on all inputs
4. **SQL Injection Prevention**: Parameterized queries
5. **CORS Configuration**: Restricted to allowed origins
6. **Error Handling**: No sensitive data in error responses
7. **HTTPS**: TLS encryption in production
8. **Rate Limiting**: API rate limiting (future enhancement)

## 📊 Monitoring & Alerting

### Metrics Collection
- Application metrics with Micrometer
- JVM metrics monitoring
- Custom business metrics

### Alerting
- Service health alerts
- Performance threshold alerts
- Error rate monitoring

### Logging
- Centralized logging with ELK stack
- Structured JSON logging
- Correlation ID tracking

## 🔄 CI/CD Pipeline

### GitHub Actions
\`\`\`yaml
# .github/workflows/ci.yml
name: CI/CD Pipeline
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK 21
        uses: actions/setup-java@v3
        with:
          java-version: '21'
      - name: Run tests
        run: mvn test
      - name: Build Docker images
        run: docker-compose build
      - name: Deploy to staging
        run: ./deploy-staging.sh
\`\`\`

## 🐛 Troubleshooting

### Common Issues

1. **Service Discovery Issues**
   \`\`\`bash
   # Check Eureka dashboard
   curl http://localhost:8761/eureka/apps
   \`\`\`

2. **Database Connection Issues**
   \`\`\`bash
   # Check database connectivity
   mysql -h localhost -u root -p -e "SHOW DATABASES;"
   \`\`\`

3. **JWT Token Issues**
   \`\`\`bash
   # Validate token
   curl -X POST http://localhost:8086/api/auth/validate-token \
     -H "Content-Type: application/json" \
     -d '{"token": "YOUR_TOKEN"}'
   \`\`\`

### Logs
\`\`\`bash
# View service logs
docker-compose logs -f user-service

# View all logs
docker-compose logs -f
\`\`\`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Write tests for new functionality
4. Implement the feature with proper validation and error handling
5. Add appropriate logging
6. Ensure all tests pass
7. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Netflix OSS for cloud-native patterns
- MySQL team for the robust database
- React team for the modern UI library

---

**CabbyAI** - Revolutionizing urban transportation with modern microservices architecture.
