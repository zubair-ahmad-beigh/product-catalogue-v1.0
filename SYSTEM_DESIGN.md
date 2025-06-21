# System Design Document

## Product Catalogue Microservice Architecture

### Overview

The Product Catalogue Microservice is a Java 8 Spring Boot application designed with a multi-version API strategy, comprehensive DevOps practices, and cloud-native deployment capabilities. The system follows microservices architecture principles with proper separation of concerns, scalability considerations, and robust error handling.

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Applications                      │
└─────────────────────┬───────────────────────────────────────────┘
                      │
┌─────────────────────▼───────────────────────────────────────────┐
│                    NGINX Ingress Controller                     │
│                    (Traffic Routing)                           │
└─────┬───────────────┬───────────────┬───────────────┬───────────┘
      │               │               │               │
┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐
│   v1.0    │   │   v1.1    │   │   v2.0    │   │  Default  │
│ Namespace │   │ Namespace │   │ Namespace │   │ (v2.0)    │
└─────┬─────┘   └─────┬─────┘   └─────┬─────┘   └─────┬─────┘
      │               │               │               │
┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐
│ Service   │   │ Service   │   │ Service   │   │ Service   │
└─────┬─────┘   └─────┬─────┘   └─────┬─────┘   └─────┬─────┘
      │               │               │               │
┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐   ┌─────▼─────┐
│ Pod       │   │ Pod       │   │ Pod       │   │ Pod       │
│ (v1.0)    │   │ (v1.1)    │   │ (v2.0)    │   │ (v2.0)    │
└───────────┘   └───────────┘   └───────────┘   └───────────┘
```

## 🔄 CI/CD Pipeline Flow

### GitHub Actions Workflow

The CI/CD pipeline follows a multi-stage approach:

1. **Build & Test Stage**
   - Java 8 environment setup
   - Maven dependency caching
   - Compilation and unit testing
   - Integration testing

2. **Docker Build Stage**
   - Multi-stage Docker build
   - Layer caching optimization
   - Security scanning with Trivy
   - Image tagging and pushing

3. **Deployment Stage**
   - Kubernetes manifest updates
   - Namespace creation
   - Deployment application
   - Health check verification

### Pipeline Stages

```
Push to Branch → Build & Test → Docker Build → Security Scan → Deploy to K8s
```

## 🏷️ Versioning Strategy

### API Versioning Approach

The system implements **URL Path Versioning** with the following benefits:

- **Clear Separation**: Each version runs in its own namespace
- **Independent Deployment**: Versions can be deployed independently
- **Backward Compatibility**: Old versions remain available
- **Gradual Migration**: Clients can migrate at their own pace

### Version Lifecycle

```
v1.0 (Stable) ←→ v1.1 (Stable) ←→ v2.0 (Latest)
     ↓              ↓              ↓
  Legacy        Transitional    Current
  Support       Support         Features
```

### Version Features Matrix

| Feature | v1.0 | v1.1 | v2.0 |
|---------|------|------|------|
| Health Check | ✅ | ✅ | ✅ |
| Product List | ✅ | ✅ | ✅ |
| Search | ❌ | ✅ | ✅ |
| Product Details | ❌ | ❌ | ✅ |
| Statistics | ❌ | ❌ | ✅ |
| Error Handling | Basic | Basic | Enhanced |
| Validation | ❌ | ❌ | ✅ |

## 🚀 Scalability Design

### Horizontal Scaling

The system is designed for horizontal scaling with:

- **Resource Limits**: CPU and memory constraints
- **Health Checks**: Liveness and readiness probes
- **Auto-scaling Ready**: HPA configuration ready
- **Load Balancing**: Kubernetes service load balancing

### Resource Management

- **CPU**: 250m request, 500m limit
- **Memory**: 512Mi request, 1Gi limit
- **JVM Heap**: 512m-1024m with G1GC
- **Container Security**: Non-root user

### Performance Optimizations

1. **Docker Multi-stage Builds**
   - Reduced image size
   - Faster builds
   - Security improvements

2. **Java 8 Optimizations**
   - G1GC garbage collector
   - Container-aware JVM settings
   - Optimized memory allocation

3. **Kubernetes Optimizations**
   - Resource limits and requests
   - Health checks and readiness probes
   - Pod disruption budgets

## 🔒 Security Architecture

### Container Security

```dockerfile
# Non-root user execution
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup
USER appuser
```

### Security Measures

1. **Vulnerability Scanning**
   - Trivy integration in CI/CD
   - Regular security updates
   - Base image scanning

2. **Input Validation**
   - Request parameter validation
   - SQL injection prevention
   - XSS protection

3. **Network Security**
   - Kubernetes network policies
   - Service mesh ready
   - TLS termination at ingress

## 📊 Monitoring & Observability

### Health Checks

```yaml
livenessProbe:
  httpGet:
    path: /health
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 30
  timeoutSeconds: 5
  failureThreshold: 3
```

### Metrics Collection

- **Application Metrics**: Spring Boot Actuator
- **JVM Metrics**: Memory, GC, threads
- **HTTP Metrics**: Request rates, response times
- **Business Metrics**: Product statistics

### Logging Strategy

- **Structured Logging**: JSON format
- **Log Levels**: Configurable per environment
- **Centralized Logging**: Ready for ELK stack
- **Correlation IDs**: Request tracing

## 🔄 Deployment Strategy

### Rolling Updates

- **Max Surge**: 25% additional pods
- **Max Unavailable**: 25% unavailable pods
- **Rollback Strategy**: Automatic rollback on failure
- **Health Check Integration**: Deployment pauses on health check failure

### Blue-Green Deployment (Future)

The system is designed to support blue-green deployments for zero-downtime updates.

## 🌐 Network Architecture

### Ingress Configuration

```yaml
# NGINX Ingress with path-based routing
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  annotations:
    kubernetes.io/ingress.class: "nginx"
    nginx.ingress.kubernetes.io/rewrite-target: /$2
spec:
  rules:
  - host: product-catalogue.local
    http:
      paths:
      - path: /v1(/|$)(.*)
        backend:
          service:
            name: product-catalogue-v1-service
      - path: /v1\.1(/|$)(.*)
        backend:
          service:
            name: product-catalogue-v1-1-service
      - path: /v2(/|$)(.*)
        backend:
          service:
            name: product-catalogue-v2-service
```

### Service Discovery

- **Internal Services**: ClusterIP for inter-service communication
- **External Access**: Ingress for external traffic
- **Load Balancing**: Kubernetes service load balancing
- **DNS Resolution**: Kubernetes DNS for service discovery

## 💾 Data Management

### Current State (Static Data)

```java
// In-memory product storage
private final List<Product> products = Arrays.asList(
    new Product(1, "Time Machine", 999.99),
    new Product(2, "Hoverboard", 299.49),
    // ... more products
);
```

### Future Database Integration

The system is designed to easily integrate with databases like PostgreSQL for persistent storage.

## 🔧 Configuration Management

### Environment-specific Configuration

```properties
# application.properties
spring.application.name=product-catalogue
app.version=2.0.0
app.java.version=8

# Environment-specific overrides
# application-dev.properties
logging.level.com.example.catalogue=DEBUG

# application-prod.properties
logging.level.com.example.catalogue=WARN
management.endpoints.web.exposure.include=health
```

### Kubernetes ConfigMaps

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: product-catalogue-config
data:
  application.properties: |
    spring.application.name=product-catalogue
    app.version=2.0.0
    logging.level.com.example.catalogue=INFO
```

## 🚨 Disaster Recovery

### Backup Strategy

1. **Application State**: Stateless design enables easy recovery
2. **Configuration**: Version-controlled configuration
3. **Database**: Regular backups (when database is added)
4. **Container Images**: Registry backup strategy

### Recovery Procedures

1. **Pod Failure**: Kubernetes automatically restarts pods
2. **Node Failure**: Pods rescheduled to healthy nodes
3. **Cluster Failure**: Multi-zone deployment (future)
4. **Data Loss**: Restore from backups (when applicable)

## 🔮 Future Enhancements

### Planned Improvements

1. **Service Mesh Integration**
   - Istio for advanced traffic management
   - Circuit breakers and retry policies
   - Distributed tracing

2. **Advanced Monitoring**
   - Prometheus metrics collection
   - Grafana dashboards
   - Alerting rules

3. **Security Enhancements**
   - mTLS between services
   - OAuth2/JWT authentication
   - API rate limiting

4. **Performance Optimizations**
   - Redis caching layer
   - Database connection pooling
   - CDN integration

### Scalability Roadmap

1. **Microservices Split**
   - Product service
   - Search service
   - Analytics service

2. **Event-Driven Architecture**
   - Apache Kafka integration
   - Event sourcing
   - CQRS pattern

3. **Cloud-Native Features**
   - Serverless functions
   - Auto-scaling groups
   - Multi-region deployment

## 📈 Performance Benchmarks

### Expected Performance

- **Response Time**: < 100ms for 95th percentile
- **Throughput**: 1000+ requests/second per pod
- **Availability**: 99.9% uptime
- **Resource Usage**: < 70% CPU, < 80% memory

### Load Testing Strategy

1. **Unit Load Tests**: Per-endpoint performance testing
2. **Integration Load Tests**: Full API workflow testing
3. **Stress Testing**: Maximum capacity testing
4. **Endurance Testing**: Long-running stability tests

This system design provides a solid foundation for a scalable, maintainable, and production-ready microservice architecture with comprehensive DevOps practices and future growth considerations. 