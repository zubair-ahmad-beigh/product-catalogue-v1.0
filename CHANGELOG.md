# Changelog

All notable changes to the Product Catalogue Microservice will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-01-15

### Added
- **Product Details Endpoint**: New `/products/{id}` endpoint to get individual products
- **Statistics Endpoint**: New `/products/stats` endpoint for product statistics
- **Enhanced Error Handling**: Comprehensive error responses with proper HTTP status codes
- **Input Validation**: Validation for search parameters and product IDs
- **Global Exception Handler**: Centralized exception handling for consistent error responses
- **Product Model**: Proper Product entity with validation annotations
- **Error Response Model**: Standardized error response format
- **Service Layer**: ProductService for business logic separation
- **Actuator Integration**: Health checks and metrics endpoints
- **Docker Multi-stage Build**: Optimized container builds
- **Kubernetes Manifests**: Complete K8s deployment configuration
- **GitHub Actions CI/CD**: Automated build, test, and deployment pipeline
- **Security Scanning**: Trivy vulnerability scanning integration

### Changed
- **Java Version**: Ensured full Java 8 compatibility (removed Java 9+ features)
- **Spring Boot**: Updated to 2.7.18 for better Java 8 support
- **Error Responses**: Standardized error format across all endpoints
- **Search Validation**: Enhanced search endpoint with proper validation
- **Health Endpoint**: Added version and Java version information

### Fixed
- **Java 8 Compatibility**: Replaced `Map.of()` and `List.of()` with Java 8 alternatives
- **Resource Management**: Proper resource limits and requests in K8s
- **Security**: Non-root user in Docker containers
- **Error Handling**: Proper HTTP status codes for different error scenarios

### Technical Debt
- **Code Organization**: Separated concerns into proper layers (Controller, Service, Model)
- **Documentation**: Comprehensive README and system design documentation
- **Testing**: Added proper test structure
- **Configuration**: Externalized configuration properties

## [1.1.0] - 2024-01-10

### Added
- **Search Functionality**: New `/products/search?keyword=...` endpoint
- **Case-insensitive Search**: Search works regardless of case
- **Product Filtering**: Filter products by name containing keyword
- **Stream API Usage**: Java 8 Streams for efficient filtering

### Changed
- **Product Data**: Expanded product list with more items
- **Search Logic**: Implemented efficient search algorithm

### Technical
- **Java 8 Streams**: Used `stream().filter().collect()` for search operations
- **Null Safety**: Proper null checking for search parameters

## [1.0.0] - 2024-01-05

### Added
- **Health Check Endpoint**: `/health` returns service status in JSON
- **Product List Endpoint**: `/products` returns static list of products
- **Basic Spring Boot Setup**: Minimal Spring Boot application
- **Java 8 Configuration**: Proper Java 8 setup in Maven
- **Static Product Data**: Initial product catalogue with 3 items

### Technical
- **Spring Boot 2.7.18**: Latest stable version supporting Java 8
- **Maven Build**: Proper Maven configuration
- **JSON Responses**: RESTful JSON API responses
- **Basic Error Handling**: Simple error responses

## [Unreleased]

### Planned
- **Database Integration**: Replace static data with database
- **Authentication**: JWT-based authentication
- **Rate Limiting**: API rate limiting
- **Caching**: Redis integration for performance
- **Logging**: Structured logging with ELK stack
- **Metrics**: Prometheus metrics integration
- **API Documentation**: OpenAPI/Swagger documentation
- **Load Testing**: Performance testing suite
- **Blue-Green Deployment**: Zero-downtime deployment strategy
- **Horizontal Pod Autoscaling**: K8s HPA configuration

### Technical Improvements
- **Microservices Architecture**: Split into multiple microservices
- **Service Mesh**: Istio integration
- **Observability**: Distributed tracing with Jaeger
- **Configuration Management**: External configuration with ConfigMaps
- **Secrets Management**: Kubernetes Secrets integration
- **Backup Strategy**: Database backup and recovery procedures

---

## Version Compatibility Matrix

| Feature | v1.0 | v1.1 | v2.0 |
|---------|------|------|------|
| Health Check | ✅ | ✅ | ✅ |
| Product List | ✅ | ✅ | ✅ |
| Search | ❌ | ✅ | ✅ |
| Product Details | ❌ | ❌ | ✅ |
| Statistics | ❌ | ❌ | ✅ |
| Error Handling | Basic | Basic | Enhanced |
| Validation | ❌ | ❌ | ✅ |
| Docker Support | ❌ | ❌ | ✅ |
| Kubernetes | ❌ | ❌ | ✅ |
| CI/CD | ❌ | ❌ | ✅ |

## Migration Guide

### From v1.0 to v1.1
- No breaking changes
- New search endpoint available
- Existing endpoints remain unchanged

### From v1.1 to v2.0
- No breaking changes to existing endpoints
- Enhanced error responses with more detailed information
- New endpoints available for additional functionality
- Improved validation for search parameters

## Deprecation Notices

- No deprecated features in current versions
- All versions maintain backward compatibility

## Security Updates

- **v2.0.0**: Added vulnerability scanning in CI/CD pipeline
- **v2.0.0**: Implemented non-root user in Docker containers
- **v2.0.0**: Added input validation to prevent injection attacks

## Performance Improvements

- **v2.0.0**: Optimized Docker multi-stage builds
- **v2.0.0**: Added resource limits in Kubernetes
- **v1.1.0**: Efficient search using Java 8 Streams 