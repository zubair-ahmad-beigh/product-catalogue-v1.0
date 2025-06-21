# Product Catalogue Microservice

A Java 8 Spring Boot microservice for managing product catalogues with multiple API versions and comprehensive DevOps setup.

## 🚀 Features

### Version 1.0
- **Health Check**: `/health` endpoint returns service status
- **Product List**: `/products` returns static product list

### Version 1.1
- **Search Functionality**: `/products/search?keyword=...` filters products by keyword (case-insensitive)

### Version 2.0
- **Enhanced Search**: Improved validation and error handling
- **Product Details**: `/products/{id}` get product by ID
- **Statistics**: `/products/stats` returns product statistics
- **Comprehensive Error Handling**: Proper HTTP status codes and error messages

## 🛠️ Technology Stack

- **Java**: 8 (Eclipse Temurin)
- **Framework**: Spring Boot 2.7.18
- **Build Tool**: Maven 3.9.4
- **Container**: Docker with multi-stage builds
- **Orchestration**: Kubernetes
- **CI/CD**: GitHub Actions
- **Security**: Trivy vulnerability scanning

## 📋 Prerequisites

- Java 8 (Eclipse Temurin recommended)
- Maven 3.6+
- Docker
- Kubernetes cluster (minikube, kind, or cloud provider)
- kubectl CLI tool

## 🏗️ Project Structure

```
product-catalogue-v1.0/
├── src/
│   └── main/
│       ├── java/com/example/catalogue/
│       │   ├── controller/
│       │   │   └── ProductController.java
│       │   ├── model/
│       │   │   ├── Product.java
│       │   │   └── ErrorResponse.java
│       │   ├── service/
│       │   │   └── ProductService.java
│       │   └── ProductCatalogueApplication.java
│       └── resources/
│           └── application.properties
├── k8s/
│   ├── namespace-*.yaml
│   ├── deployment-*.yaml
│   ├── service-*.yaml
│   └── ingress.yaml
├── .github/workflows/
│   └── ci-cd.yml
├── Dockerfile
├── .dockerignore
├── pom.xml
└── README.md
```

## 🚀 Quick Start

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd product-catalogue-v1.0
   ```

2. **Build the application**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Test the endpoints**
   ```bash
   # Health check
   curl http://localhost:8082/health
   
   # Get all products
   curl http://localhost:8082/products
   
   # Search products (v1.1+)
   curl http://localhost:8082/products/search?keyword=machine
   
   # Get product by ID (v2.0+)
   curl http://localhost:8082/products/1
   
   # Get statistics (v2.0+)
   curl http://localhost:8082/products/stats
   ```

### Docker Build

1. **Build Docker image**
   ```bash
   docker build -t product-catalogue:latest .
   ```

2. **Run container**
   ```bash
   docker run -p 8082:8082 product-catalogue:latest
   ```

### Kubernetes Deployment

1. **Create namespaces**
   ```bash
   kubectl apply -f k8s/namespace-v1.yaml
   kubectl apply -f k8s/namespace-v1-1.yaml
   kubectl apply -f k8s/namespace-v2.yaml
   ```

2. **Deploy applications**
   ```bash
   # Deploy v1.0
   kubectl apply -f k8s/deployment-v1.yaml
   kubectl apply -f k8s/service-v1.yaml
   
   # Deploy v1.1
   kubectl apply -f k8s/deployment-v1-1.yaml
   kubectl apply -f k8s/service-v1-1.yaml
   
   # Deploy v2.0
   kubectl apply -f k8s/deployment-v2.yaml
   kubectl apply -f k8s/service-v2.yaml
   ```

3. **Deploy ingress**
   ```bash
   kubectl apply -f k8s/ingress.yaml
   ```

4. **Access the application**
   ```bash
   # v1.0 endpoints
   curl http://your-domain/v1/health
   curl http://your-domain/v1/products
   
   # v1.1 endpoints
   curl http://your-domain/v1.1/health
   curl http://your-domain/v1.1/products
   curl http://your-domain/v1.1/products/search?keyword=machine
   
   # v2.0 endpoints
   curl http://your-domain/v2/health
   curl http://your-domain/v2/products
   curl http://your-domain/v2/products/search?keyword=machine
   curl http://your-domain/v2/products/1
   curl http://your-domain/v2/products/stats
   ```

## 🔧 Configuration

### Environment Variables

- `JAVA_OPTS`: JVM options (default: `-Xms512m -Xmx1024m -XX:+UseG1GC`)
- `SPRING_PROFILES_ACTIVE`: Spring profile (default: `default`)
- `SERVER_PORT`: Application port (default: `8082`)

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8082
server.servlet.context-path=/

# Application Information
spring.application.name=product-catalogue
app.version=2.0.0
app.java.version=8

# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=always
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

### API Testing
```bash
# Test health endpoint
curl -X GET http://localhost:8082/health

# Test products endpoint
curl -X GET http://localhost:8082/products

# Test search endpoint (v1.1+)
curl -X GET "http://localhost:8082/products/search?keyword=machine"

# Test error handling (v2.0+)
curl -X GET "http://localhost:8082/products/search"
curl -X GET http://localhost:8082/products/999
```

## 🔒 Security

- **Container Security**: Non-root user in Docker container
- **Vulnerability Scanning**: Trivy integration in CI/CD pipeline
- **Input Validation**: Comprehensive validation for all inputs
- **Error Handling**: Secure error messages without sensitive information

## 📊 Monitoring

### Health Checks
- **Liveness Probe**: `/health` endpoint
- **Readiness Probe**: `/health` endpoint
- **Actuator Endpoints**: `/actuator/health`, `/actuator/metrics`

### Metrics
- Application metrics via Spring Boot Actuator
- JVM metrics
- HTTP request metrics

## 🚀 CI/CD Pipeline

The GitHub Actions workflow includes:

1. **Build & Test**: Maven build with Java 8
2. **Docker Build**: Multi-stage Docker build
3. **Security Scan**: Trivy vulnerability scanning
4. **Deploy**: Kubernetes deployment based on branch

### Required Secrets

Configure these secrets in your GitHub repository:

- `DOCKER_USERNAME`: Docker Hub username
- `DOCKER_PASSWORD`: Docker Hub password/token
- `KUBE_CONFIG`: Base64 encoded kubeconfig file

## 🔄 Version Management

### API Versions

- **v1.0**: Basic functionality
- **v1.1**: Added search capability
- **v2.0**: Enhanced error handling and additional endpoints

### Branch Strategy

- `main`: Latest stable version
- `v1.0`: Version 1.0.x releases
- `v1.1`: Version 1.1.x releases
- `v2.0`: Version 2.0.x releases

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the documentation in `/docs` folder
- Review the CHANGELOG.md for recent changes 