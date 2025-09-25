# 🐳 Docker Configuration

This directory contains Docker configuration files and documentation for the Enterprise Resource Planning System.

## 📋 Overview

The ERP system is designed to run efficiently in containerized environments using Docker and Docker Compose. This setup provides:

- Consistent development and production environments
- Easy deployment and scaling
- Isolated service dependencies
- Simplified configuration management

## 🏗️ Architecture

### Services

- **erp-app**: Main Spring Boot application
- **mysql**: MySQL 8.0 database server
- **redis**: Redis cache server (optional)
- **nginx**: Reverse proxy (production only)

## 🚀 Quick Start

### Prerequisites

- Docker 20.10+
- Docker Compose 2.0+
- At least 4GB RAM available
- Ports 8080, 3306, 6379 available

### Development Environment

```bash
# Clone the repository
git clone https://github.com/galafis/Enterprise-Resource-Planning-System.git
cd Enterprise-Resource-Planning-System

# Start development environment
docker-compose up -d

# View logs
docker-compose logs -f erp-app

# Access the application
open http://localhost:8080
```

### Production Environment

```bash
# Build production image
docker build -t erp-system:latest .

# Start production stack
docker-compose -f docker-compose.prod.yml up -d

# Check status
docker-compose ps
```

## 📁 Files Structure

```
docker/
├── README.md              # This file
├── Dockerfile.prod        # Production Dockerfile
├── docker-compose.yml     # Development compose file
├── docker-compose.prod.yml # Production compose file
├── nginx/
│   ├── nginx.conf         # Nginx configuration
│   └── ssl/               # SSL certificates
├── mysql/
│   ├── init.sql           # Database initialization
│   └── my.cnf             # MySQL configuration
└── scripts/
    ├── build.sh           # Build script
    ├── deploy.sh          # Deployment script
    └── backup.sh          # Database backup script
```

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `dev` | No |
| `DB_HOST` | Database host | `mysql` | Yes |
| `DB_PORT` | Database port | `3306` | No |
| `DB_NAME` | Database name | `erp_system` | Yes |
| `DB_USERNAME` | Database username | `erp_user` | Yes |
| `DB_PASSWORD` | Database password | - | Yes |
| `REDIS_HOST` | Redis host | `redis` | No |
| `REDIS_PORT` | Redis port | `6379` | No |
| `JWT_SECRET` | JWT signing secret | - | Yes |
| `LOG_LEVEL` | Application log level | `INFO` | No |

### Database Configuration

The MySQL service is configured with:
- UTF8MB4 character set
- Optimized memory settings
- Automatic backup scheduling
- Health checks

### Redis Configuration

Redis is configured for:
- Session storage
- Application caching
- Pub/Sub messaging
- Persistence enabled

## 🔍 Monitoring

### Health Checks

All services include health checks:

```bash
# Check service health
docker-compose ps

# View health check logs
docker inspect --format='{{.State.Health.Status}}' container_name
```

### Logs

```bash
# Application logs
docker-compose logs -f erp-app

# Database logs
docker-compose logs -f mysql

# All services
docker-compose logs -f
```

### Metrics

The application exposes metrics at:
- `/actuator/health` - Health status
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

## 🚀 Deployment

### Development

```bash
# Start all services
docker-compose up -d

# Scale application instances
docker-compose up -d --scale erp-app=3

# Update single service
docker-compose up -d erp-app
```

### Production

```bash
# Deploy to production
./docker/scripts/deploy.sh production

# Rolling update
docker service update --image erp-system:latest erp_erp-app

# Rollback
docker service rollback erp_erp-app
```

## 🔐 Security

### Best Practices

- Use non-root users in containers
- Scan images for vulnerabilities
- Use secrets management
- Enable network isolation
- Regular security updates

### SSL/TLS

For production deployments:
1. Place SSL certificates in `docker/nginx/ssl/`
2. Update `nginx.conf` with certificate paths
3. Redirect HTTP to HTTPS

## 🛠️ Troubleshooting

### Common Issues

**Container won't start:**
```bash
docker-compose logs container_name
```

**Database connection errors:**
```bash
# Check database is running
docker-compose ps mysql

# Verify network connectivity
docker-compose exec erp-app ping mysql
```

**Performance issues:**
```bash
# Check resource usage
docker stats

# View container limits
docker inspect container_name | grep -A 10 Resources
```

### Database Recovery

```bash
# Create backup
./docker/scripts/backup.sh

# Restore backup
docker-compose exec mysql mysql -u root -p erp_system < backup.sql
```

## 📊 Performance Tuning

### JVM Settings

For production, adjust JVM settings in docker-compose:
```yaml
services:
  erp-app:
    environment:
      - JAVA_OPTS=-Xmx2g -Xms1g -XX:+UseG1GC
```

### Database Optimization

- Adjust MySQL memory settings
- Configure connection pooling
- Enable query caching
- Regular maintenance tasks

## 🔄 CI/CD Integration

Example GitHub Actions workflow:
```yaml
name: Docker Build and Deploy
on:
  push:
    branches: [main]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Build and push Docker image
        run: |
          docker build -t erp-system:${{ github.sha }} .
          docker push erp-system:${{ github.sha }}
```

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Reference](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [MySQL Docker Hub](https://hub.docker.com/_/mysql)

---

**Author**: Gabriel Demetrios Lafis  
**Last Updated**: September 2025  
**Version**: 1.0
