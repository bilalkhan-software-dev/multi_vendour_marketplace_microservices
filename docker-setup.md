# Vendor Marketplace Microservices - Local Development Guide

## 📋 Prerequisites

### System Requirements
- **Docker Desktop** (or Docker Engine + Docker Compose)
- Minimum **8GB RAM** (16GB recommended)
- **10GB free disk space**
- Git

### Verify Docker Installation
```bash
# Check Docker version
docker --version
docker-compose --version

# Ensure Docker is running
docker info
```

## 🚀 Quick Start (One-Command Setup)

### Option 1: Start Everything at Once
```bash
# Clone the repository
git clone https://github.com/bilalkhan-software-dev/multi_vendour_marketplace_microservices.git

# Navigate to docker-deployment
cd multi_vendour_marketplace_microservices/docker-deployment

# Pull latest images
docker compose pull

# Start all services
docker compose up -d
```

### Option 2: Start Services in Stages (Recommended for first-time setup)
```bash
# 1. Start infrastructure services first
docker compose up -d zookeeper kafka-broker-1 kafka-broker-2

# 2. Start databases
docker compose up -d \
  auth-service-postgres \
  user-service-postgres \
  seller-service-postgres \
  order-service-postgres \
  payment-service-postgres \
  review-wishlist-service-postgres \
  transaction-report-service-postgres \
  mysql \
  home-service-mongodb \
  product-command-service-mongodb \
  product-query-service-mongodb \
  redis

# Wait 60 seconds for databases to initialize
sleep 60

# 3. Start core services
docker compose up -d \
  eureka-server \
  config-server \
  gateway-server

# Wait 30 seconds for service discovery to initialize
sleep 30

# 4. Start business services
docker compose up -d \
  auth-service \
  user-service \
  seller-service \
  order-service \
  payment-service \
  review-wishlist-service \
  transaction-report-service \
  notification-service \
  cart-service \
  product-command-service \
  product-query-service \
  home-service

# 5. Start monitoring tools (optional)
docker compose up -d kafka-ui
```

## 🔍 Verify Installation

### Check Running Services
```bash
# List all running containers
docker ps

# Check service health
docker compose ps

# View logs for a specific service
docker compose logs auth-service
docker compose logs eureka-server

# Follow logs in real-time
docker compose logs -f auth-service
```

### Access Service Dashboards

| Service | URL | Port | Purpose |
|---------|-----|------|---------|
| **Eureka Server** | http://localhost:8761 | 8761 | Service Discovery Dashboard |
| **Kafka UI** | http://localhost:2006 | 2006 | Kafka Cluster Monitoring |
| **Gateway** | http://localhost:2008 | 2008 | API Gateway |
| **Auth Service** | http://localhost:8082 | 8082 | Authentication API |
| **User Service** | http://localhost:8080 | 8080 | User Management API |
| **Seller Service** | http://localhost:8081 | 8081 | Seller Management API |

## ⚙️ Configuration

### Environment Variables
Create a `.env` file for custom configurations:
```env
# Database credentials
POSTGRES_PASSWORD=khan
MYSQL_ROOT_PASSWORD=rootpassword
MONGO_INITDB_ROOT_PASSWORD=password123

# Kafka settings
KAFKA_BROKERS=localhost:29092,localhost:29093

# Email settings (for notification service)
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# Stripe (for payment service)
STRIPE_SECRET_KEY=sk_test_your_stripe_key
```

### Port Mapping Reference

| Service | Container Port | Host Port |
|---------|---------------|-----------|
| Zookeeper | 2181 | 2181 |
| Kafka Broker 1 | 9092 | 9092 |
| Kafka Broker 1 (External) | 29092 | 29092 |
| Kafka Broker 2 | 9093 | 9093 |
| Kafka Broker 2 (External) | 29093 | 29093 |
| Kafka UI | 8080 | 2006 |
| Eureka | 8761 | 8761 |
| Config Server | 2007 | 2007 |
| Gateway | 2008 | 2008 |
| Auth Service | 8082 | 8082 |
| User Service | 8080 | 8080 |
| Seller Service | 8081 | 8081 |
| Order Service | 8088 | 8088 |
| Payment Service | 8089 | 8089 |
| Review Service | 8090 | 8090 |
| Transaction Service | 8087 | 8087 |
| Notification Service | 8083 | 8083 |
| Cart Service | 8086 | 8086 |
| Product Command | 8084 | 8084 |
| Product Query | 8085 | 8085 |
| Home Service | 8091 | 8091 |

## 🗄️ Database Access

### PostgreSQL Databases
```bash
# Connect to auth service database
docker exec -it auth-service-postgres psql -U postgres -d auth-service-db

# Connect to user service database  
docker exec -it user-service-postgres psql -U postgres -d user-service-db

# List all databases
docker exec -it auth-service-postgres psql -U postgres -c "\l"
```

### MySQL Database
```bash
# Connect to cart service database
docker exec -it cart-service-mysql mysql -u admin -p cart-service-db
# Password: 1234567
```

### MongoDB Databases
```bash
# Connect to product command service MongoDB
docker exec -it product-command-service-mongodb mongosh -u admin -p password123 --authenticationDatabase admin

# Connect to home service MongoDB
docker exec -it home-service-mongodb mongosh -u admin -p password123 --authenticationDatabase admin
```

### Redis
```bash
# Connect to Redis CLI
docker exec -it auth-service-redis redis-cli

# Check Redis info
docker exec -it auth-service-redis redis-cli info
```

## 🛠️ Common Operations

### Start/Stop Services
```bash
# Start specific service
docker compose start auth-service

# Stop specific service  
docker compose stop auth-service

# Restart service
docker compose restart auth-service

# Stop all services
docker compose down

# Stop and remove volumes (CAUTION: deletes all data)
docker compose down -v
```

### View Logs
```bash
# View logs for all services
docker compose logs

# View logs for specific service
docker compose logs auth-service
docker compose logs kafka-broker-1

# Follow logs (real-time)
docker compose logs -f auth-service

# View last 100 lines
docker compose logs --tail=100 auth-service

# View logs with timestamps
docker compose logs -t auth-service
```

### Monitor Resources
```bash
# Check container resource usage
docker stats

# Check disk usage
docker system df

# Prune unused resources
docker system prune -a
```

## 🔧 Troubleshooting

### Common Issues & Solutions

**Issue: Services not starting**
```bash
# Check if ports are in use
netstat -ano | findstr :8082

# Kill process using port (Windows)
taskkill /PID <PID> /F

# Restart Docker Desktop
# Then restart services:
docker compose down
docker compose up -d
```

**Issue: Database connection errors**
```bash
# Check if database is running
docker compose ps | grep postgres

# Check database logs
docker compose logs auth-service-postgres

# Restart database
docker compose restart auth-service-postgres
```

**Issue: Kafka connection issues**
```bash
# Check Kafka brokers
docker compose logs kafka-broker-1
docker compose logs kafka-broker-2

# Check Zookeeper
docker compose logs zookeeper

# Restart Kafka cluster
docker compose restart zookeeper kafka-broker-1 kafka-broker-2
```

**Issue: Service discovery problems**
```bash
# Check Eureka server
curl http://localhost:8761/eureka/apps

# Check if services are registered
open http://localhost:8761
```

### Health Checks
```bash
# Check Eureka server
curl http://localhost:8761/actuator/health

# Check Config server
curl http://localhost:2007/actuator/health

# Check Gateway
curl http://localhost:2008/actuator/health

# Check Auth service
curl http://localhost:8082/actuator/health
```

## 📊 Monitoring

### Access Monitoring Tools
1. **Eureka Dashboard**: http://localhost:8761
2. **Kafka UI**: http://localhost:2006
3. **Service Health Endpoints**: `/actuator/health` on each service

### Check Service Status
```bash
# List all registered services in Eureka
curl -s http://localhost:8761/eureka/apps | grep -o '<name>[^<]*</name>' | sed 's/<name>//g' | sed 's/<\/name>//g'

# Check individual service health
for port in 8080 8081 8082 8083 8084 8085 8086 8087 8088 8089 8090 8091; do
  echo "Port $port: $(curl -s -o /dev/null -w "%{http_code}" http://localhost:$port/actuator/health)"
done
```

## 🧹 Cleanup

### Stop All Services
```bash
# Stop all services (preserves data)
docker compose down

# Stop and remove all data (CAUTION)
docker compose down -v

# Remove all containers, images, volumes
docker system prune -a --volumes
```

### Reset Specific Database
```bash
# Reset auth service database
docker compose stop auth-service
docker volume rm docker-deployment_auth_postgres_data
docker compose up -d auth-service-postgres
docker compose up -d auth-service
```

## 📚 Useful Commands Cheat Sheet

```bash
# Quick reference
docker compose up -d                    # Start all services
docker compose down                     # Stop all services
docker compose logs -f [service]        # View logs
docker compose restart [service]        # Restart service
docker compose ps                       # Check status
docker compose pull                     # Update images

# Database access
docker exec -it [container] [command]   # Execute in container

# Monitoring
docker stats                            # Resource usage
docker system df                        # Disk usage
```

## 🆘 Need Help?

1. **Check logs**: `docker compose logs [service-name]`
2. **Verify ports**: Ensure no port conflicts
3. **Check resources**: Ensure Docker has enough memory (8GB+)
4. **Restart Docker**: Sometimes a simple restart helps
5. **Clear Docker cache**: `docker system prune -a`

---

**Note**: First-time startup may take 5-10 minutes as databases initialize and services register with Eureka. Monitor logs with `docker compose logs -f` to track progress.