# 🍔 QuickFood - Distributed Food Delivery Platform

![QuickFood](https://img.shields.io/badge/QuickFood-Distributed%20Food%20Delivery-blue)![Java](https://img.shields.io/badge/Java-17%2F21-orange)![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen)![Microservices](https://img.shields.io/badge/Architecture-Microservices-red)![Apache Kafka](https://img.shields.io/badge/Message%20Broker-Apache%20Kafka-black)![Redis](https://img.shields.io/badge/Cache-Redis-red)![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)![Docker](https://img.shields.io/badge/Container-Docker-2496ED)**Một hệ thống đặt đồ ăn và giao hàng trực tuyến (tương tự GrabFood, ShopeeFood) được xây dựng dựa trên Kiến trúc Microservices.**

---

## 📋 Mục lục

- [Giới thiệu](#-gi%E1%BB%9Bi-thi%E1%BB%87u)
- [Công nghệ sử dụng](#-c%C3%B4ng-ngh%E1%BB%87-s%E1%BB%AD-d%E1%BB%A5ng)
- [Kiến trúc hệ thống](#-ki%E1%BA%BFn-tr%C3%BAc-h%E1%BB%87-th%E1%BB%91ng)
- [Chi tiết các Microservices](#-chi-ti%E1%BA%BFt-c%C3%A1c-microservices)
- [Kiến trúc Kafka (Event-Driven)](#-ki%E1%BA%BFn-tr%C3%BAc-kafka-event-driven)
- [Redis Cache & Realtime](#-redis-cache--realtime)
- [API Gateway](#-api-gateway)
- [Realtime Tracking](#-realtime-tracking)
- [Distributed Transaction (Saga Pattern)](#-distributed-transaction-saga-pattern)
- [Monitoring & Distributed Tracing](#-monitoring--distributed-tracing)
- [Yêu cầu môi trường](#-y%C3%AAu-c%E1%BA%A7u-m%C3%B4i-tr%C6%B0%E1%BB%9Dng)
- [Hướng dẫn cài đặt và chạy dự án](#-h%C6%B0%E1%BB%9Bng-d%E1%BA%ABn-c%C3%A0i-%C4%91%E1%BA%B7t-v%C3%A0-ch%E1%BA%A1y-d%E1%BB%B1-%C3%A1n)
- [Cấu trúc thư mục](#-c%E1%BA%A5u-tr%C3%BAc-th%C6%B0-m%E1%BB%A5c)
- [API Endpoints](#-api-endpoints)
- [Lộ trình phát triển](#-l%E1%BB%99-tr%C3%ACnh-ph%C3%A1t-tri%E1%BB%83n)
- [Mục tiêu học tập](#-m%E1%BB%A5c-ti%C3%AAu-h%E1%BB%8Dc-t%E1%BA%ADp)
- [Đóng góp](#-%C4%91%C3%B3ng-g%C3%B3p)

---

## 🎯 Giới thiệu

**QuickFood** là một nền tảng giao đồ ăn trực tuyến phân tán được xây dựng với kiến trúc **Microservices**. Dự án tập trung vào việc giải quyết các bài toán của hệ thống phân tán trong thực tế như:

- **Realtime Tracking** - Theo dõi vị trí tài xế theo thời gian thực qua WebSocket & Redis
- **Event-Driven Processing** - Xử lý sự kiện bất đồng bộ với Apache Kafka
- **Caching** - Tối ưu hiệu năng với Redis Cache
- **API Gateway** - Cổng vào duy nhất cho tất cả request
- **Distributed Transactions (Saga Pattern)** - Xử lý giao dịch phân tán đảm bảo tính nhất quán cuối cùng
- **Database-per-Service** - Mỗi service có cơ sở dữ liệu riêng biệt
- **Monitoring & Tracing** - Giám sát và theo dõi phân tán với Prometheus, Grafana, Zipkin

---

## 🚀 Công nghệ sử dụng

| Công nghệ | Mục đích |
| --- | --- |
| **Java 17/21** | Ngôn ngữ lập trình backend |
| **Spring Boot 3.2.x** | Framework xây dựng microservices |
| **Spring Cloud Gateway** | API Gateway |
| **Spring Security + JWT** | Xác thực và phân quyền |
| **PostgreSQL** | Cơ sở dữ liệu quan hệ (Database-per-service) |
| **Apache Kafka** | Message broker cho kiến trúc event-driven |
| **Redis** | Cache dữ liệu và lưu trữ vị trí realtime |
| **Docker & Docker Compose** | Container hóa và orchestration |
| **Maven** | Build tool và quản lý dependencies |
| **Prometheus + Grafana** | Monitoring và trực quan hóa metrics |
| **Zipkin** | Distributed tracing |

---

## 🏗 Kiến trúc hệ thống

### Kiến trúc tổng thể

```txt
                                ┌─────────────────────────────┐
                                │     Frontend (React/Flutter) │
                                └─────────────┬───────────────┘
                                              │
                                ┌─────────────▼───────────────┐
                                │    Nginx Load Balancer       │
                                └─────────────┬───────────────┘
                                              │
                                ┌─────────────▼───────────────┐
                                │   Spring Cloud Gateway      │
                                │        (Port 8080)           │
                                └─────────────┬───────────────┘
                                              │
        ┌──────────┬──────────┬──────────┬────┴────┬──────────┬──────────┐
        ▼          ▼          ▼          ▼         ▼          ▼          ▼
   ┌─────────┐┌─────────┐┌─────────┐┌─────────┐┌─────────┐┌─────────┐┌─────────┐
   │  Auth   ││  User   ││Restaurant││  Menu   ││  Order  ││ Payment ││  Driver │
   │ Service ││ Service ││ Service  ││ Service ││ Service ││ Service ││ Service │
   │         ││         ││          ││         ││         ││         ││         │
   │:8081    ││:8082    ││:8083     ││:8084    ││:8085    ││:8086    ││:8087    │
   └────┬────┘└────┬────┘└────┬─────┘└────┬────┘└────┬────┘└────┬────┘└────┬────┘
        │          │          │           │          │          │          │
   ┌────▼────┐┌────▼────┐┌────▼─────┐┌────▼────┐┌────▼────┐┌────▼────┐┌────▼────┐
   │Postgres ││Postgres ││Postgres  ││Postgres ││Postgres ││Postgres ││Postgres │
   └─────────┘└─────────┘└──────────┘└─────────┘└─────────┘└─────────┘└─────────┘
        │          │          │           │          │          │          │
        └──────────┴──────────┴───────────┴──────────┴──────────┴──────────┴──────────┐
                                                                                      │
                                                        ┌─────────────────────────────▼──┐
                                                        │      Apache Kafka            │
                                                        │  (Event Bus / Message Broker) │
                                                        └─────────────────────────────▲──┘
                                                                                      │
                                                        ┌─────────────────────────────▼──┐
                                                        │      Redis Cache              │
                                                        │  (Cache + Realtime Location)  │
                                                        └─────────────────────────────▲──┘
                                                                                      │
                                                        ┌─────────────────────────────▼──┐
                                                        │ Prometheus | Grafana | Zipkin│
                                                        │    (Monitoring & Tracing)     │
                                                        └───────────────────────────────┘
```

### Database-per-Service Pattern

Mỗi microservice sở hữu cơ sở dữ liệu PostgreSQL riêng, đảm bảo tính độc lập và loose coupling giữa các service.

---

## 📦 Chi tiết các Microservices

### 1️⃣ Auth Service (Port 8081)

Xử lý xác thực và phân quyền người dùng.

**Database:**

- `users` - Thông tin người dùng
- `roles` - Vai trò (CUSTOMER, DRIVER, RESTAURANT, ADMIN)
- `tokens` - Lưu trữ JWT token

**API Endpoints:**

| Method | Endpoint | Mô tả |
| --- | --- | --- |
| POST | `/api/auth/register` | Đăng ký tài khoản |
| POST | `/api/auth/login` | Đăng nhập |
| POST | `/api/auth/refresh` | Refresh JWT token |
| POST | `/api/auth/forgot-password` | Quên mật khẩu |
| POST | `/api/auth/oauth/login` | Đăng nhập OAuth |

**JWT Token:**

- **Access Token:** Thời hạn ngắn (15 phút), dùng để xác thực request
- **Refresh Token:** Thời hạn dài (7 ngày), dùng để lấy Access Token mới

---

### 2️⃣ User Service (Port 8082)

Quản lý hồ sơ người dùng.

**Database:**

- `user_profiles` - Thông tin chi tiết người dùng
- `addresses` - Địa chỉ giao hàng
- `payment_methods` - Phương thức thanh toán

**Chức năng:**

- Xem/Sửa hồ sơ cá nhân
- Quản lý địa chỉ giao hàng
- Lưu phương thức thanh toán

---

### 3️⃣ Restaurant Service (Port 8083)

Quản lý thông tin nhà hàng.

**Database:**

- `restaurants` - Thông tin nhà hàng
- `categories` - Danh mục món ăn

**Chức năng:**

- CRUD nhà hàng
- Quản lý danh mục
- Xem đơn hàng của nhà hàng
- Thống kê doanh thu

---

### 4️⃣ Menu Service (Port 8084)

Quản lý thực đơn của nhà hàng, kết hợp Redis cache.

**Database:**

- `menus` - Thực đơn
- `foods` - Món ăn

**Redis Cache:**

- Key: `menu:{restaurantId}`
- TTL: 10 phút

**Chức năng:**

- Thêm/Sửa/Xóa món ăn
- Bật/Tắt trạng thái món
- Chỉnh giá
- Quản lý khuyến mãi

---

### 5️⃣ Order Service (Port 8085) - 🎯 Core Service

Trái tim của hệ thống, quản lý toàn bộ vòng đời đơn hàng.

**Database:**

- `orders` - Đơn hàng
- `order_items` - Chi tiết đơn hàng

**Order States (Vòng đời đơn hàng):**

```txt
    ┌──────────┐
    │  CREATED │
    └────┬─────┘
         ▼
    ┌──────────┐
    │ CONFIRMED│
    └────┬─────┘
         ▼
    ┌──────────┐
    │PREPARING │
    └────┬─────┘
         ▼
    ┌──────────┐
    │  READY   │
    └────┬─────┘
         ▼
    ┌────────────┐
    │DRIVER_ASSIGNED│
    └────┬─────────┘
         ▼
    ┌──────────┐
    │ PICKED_UP│
    └────┬─────┘
         ▼
    ┌───────────┐
    │DELIVERING │
    └────┬──────┘
         ▼
    ┌───────────┐
    │ COMPLETED │
    └───────────┘

    ┌───────────┐
    │ CANCELLED │ (Có thể hủy ở bất kỳ trạng thái nào)
    └───────────┘
```

**Kafka Events Publish:**

- `OrderCreated` - Khi đơn hàng được tạo
- `OrderCancelled` - Khi đơn hàng bị hủy

---

### 6️⃣ Payment Service (Port 8086)

Xử lý thanh toán với nhiều phương thức.

**Database:**

- `payments` - Giao dịch thanh toán
- `transactions` - Lịch sử giao dịch

**Phương thức thanh toán hỗ trợ:**

- 💵 COD (Cash on Delivery)
- 💳 VNPay
- 📱 MoMo
- 🌍 Stripe

**Kafka Events:**

- `PaymentSuccess` - Thanh toán thành công
- `PaymentFailed` - Thanh toán thất bại

---

### 7️⃣ Driver Service (Port 8087)

Quản lý tài xế và vị trí realtime.

**Database:**

- `drivers` - Thông tin tài xế

**Redis (Realtime Location):**

```txt
driver:location:{id} → { lat, lng, updatedAt }
```

**Chức năng:**

- Online/Offline
- Nhận/Từ chối đơn hàng
- Update vị trí GPS (realtime)
- Hoàn thành giao hàng

---

### 8️⃣ Notification Service

Gửi thông báo đến người dùng qua Kafka Consumer.

**Kafka Topics Consume:**

- `OrderCreated`
- `DriverAssigned`
- `Delivered`
- `Promotion`

**Loại thông báo:**

- Push notification
- Email
- SMS (dự kiến)

---

### 9️⃣ Review Service

Đánh giá và nhận xét.

**Database:**

- `reviews` - Đánh giá

**Chức năng:**

- ⭐ Đánh giá tài xế
- ⭐ Đánh giá món ăn
- ⭐ Đánh giá nhà hàng

---

### 🔟 Location Service & Tracking Service

- **Location Service:** Xử lý dữ liệu vị trí GPS từ tài xế
- **Tracking Service:** Cung cấp API cho frontend lấy vị trí realtime

---

## 📨 Kiến trúc Kafka (Event-Driven)

### Luồng sự kiện điển hình

```txt
  Order Service
       │
       ▼
  OrderCreated Event
       │
       ▼
  ┌──────────────────┐
  │     Kafka         │
  │  (Event Broker)   │
  └──────────────────┘
       │
       ├────────────────────────────────┐
       ▼                                ▼
  Payment Service                 Notification Service
       │                                │
       ▼                                │
  PaymentSuccess Event                  │
       │                                │
       ▼                                │
  ┌──────────┐                          │
  │  Kafka   │                          │
  └──────────┘                          │
       │                                │
       ▼                                │
  Driver Service                        │
       │                                │
       ▼                                │
  DriverAssigned Event ─────────────────►│
       │                                │
       ▼                                ▼
  Notification Service           Gửi thông báo đến User
       │
       ▼
  Gửi thông báo đến Driver
```

### Kafka Topics

| Topic | Producer | Consumer(s) | Mô tả |
| --- | --- | --- | --- |
| `order-created` | Order Service | Payment, Notification | Đơn hàng mới được tạo |
| `payment-success` | Payment Service | Driver, Notification | Thanh toán thành công |
| `payment-failed` | Payment Service | Order Service | Thanh toán thất bại |
| `driver-assigned` | Driver Service | Notification | Tài xế đã được chỉ định |
| `delivery-started` | Driver Service | Notification | Bắt đầu giao hàng |
| `delivery-completed` | Driver Service | Order, Notification | Giao hàng hoàn thành |

### Dead Letter Queue (DLQ)

Các message xử lý thất bại được chuyển vào DLQ để debug và xử lý lại sau:

- `order-created-dlq`
- `payment-success-dlq`
- `payment-failed-dlq`

---

## ⚡ Redis Cache & Realtime

### Cache Menu

```java
// Cache menu của nhà hàng trong 10 phút
redisTemplate.opsForValue().set("menu:" + restaurantId, menuData, 10, TimeUnit.MINUTES);
```

### Driver Location Realtime

```txt
Key:   driver:location:123
Value: {
  "lat": 10.762622,
  "lng": 106.660172,
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

### Rate Limiting (API Gateway)

```txt
100 requests/minute/user
```

### ETA Cache

Lưu thời gian dự kiến giao hàng để tránh tính toán lại quá nhiều lần.

---

## 🔀 API Gateway

**Spring Cloud Gateway** đóng vai trò là cổng vào duy nhất cho tất cả request từ client.

### Routing Rules

| Path | Target Service | Port |
| --- | --- | --- |
| `/api/auth/**` | Auth Service | 8081 |
| `/api/users/**` | User Service | 8082 |
| `/api/restaurants/**` | Restaurant Service | 8083 |
| `/api/menus/**` | Menu Service | 8084 |
| `/api/orders/**` | Order Service | 8085 |
| `/api/payments/**` | Payment Service | 8086 |
| `/api/drivers/**` | Driver Service | 8087 |
| `/api/reviews/**` | Review Service | 8088 |
| `/api/tracking/**` | Tracking Service | 8089 |
| `/ws/**` | WebSocket (Realtime) | \- |

### Tính năng của Gateway

- ✅ **Authentication** - Xác thực JWT token cho tất cả request
- ✅ **Routing** - Định tuyến request đến service phù hợp
- ✅ **Logging** - Ghi log tất cả request/response
- ✅ **Rate Limiting** - Giới hạn số lượng request

---

## 📍 Realtime Tracking

### Kiến trúc Realtime

```txt
   Driver App
      │
      │ (GPS Update HTTP)
      ▼
   Driver Service
      │
      ▼
   Redis (driver:location:{id})
      │
      │ (Pub/Sub)
      ▼
   WebSocket Server
      │
      │ (WebSocket Connection)
      ▼
   User App (Frontend)
```

### Luồng hoạt động

1. **Driver App** gửi GPS location lên Driver Service (mỗi 3-5 giây)
2. **Driver Service** lưu vào Redis với key `driver:location:{id}`
3. **WebSocket Server** publish location update đến các client đang theo dõi
4. **User App** nhận và hiển thị vị trí tài xế realtime trên bản đồ

---

## 🔄 Distributed Transaction (Saga Pattern)

### Saga Pattern - Choreography

Khi một đơn hàng được tạo, nhiều service phải phối hợp với nhau. Nếu một bước thất bại, tất cả các bước trước đó phải được rollback.

### Luồng Saga thành công

```txt
Order Service: Tạo đơn hàng (CREATED)
      │
      ▼
Payment Service: Xử lý thanh toán
      │
      ▼
Driver Service: Chỉ định tài xế
      │
      ▼
Notification Service: Gửi thông báo
      │
      ▼
Order Service: Cập nhật trạng thái (COMPLETED)
```

### Luồng Saga thất bại (Rollback)

```txt
Order Service: Tạo đơn hàng (CREATED)
      │
      ▼
Payment Service: ❌ Thanh toán thất bại
      │
      ▼
Order Service: Hủy đơn hàng (CANCELLED)
      │
      ▼
Payment Service: Hoàn tiền (Refund)
      │
      ▼
Notification Service: Gửi thông báo hủy đơn
```

> **Nguyên tắc:** Payment chết → Order vẫn tồn tại → Saga rollback. Đảm bảo **Eventually Consistent** giữa các service.

---

## 📊 Monitoring & Distributed Tracing

### Prometheus

Thu thập metrics từ tất cả microservices:

- CPU Usage
- Memory Usage
- Request throughput
- Error rate
- Response time (P50, P95, P99)

### Grafana

Trực quan hóa dữ liệu từ Prometheus với dashboard:

- System Overview Dashboard
- Service Health Dashboard
- Business Metrics Dashboard

### Zipkin - Distributed Tracing

Theo dõi luồng request xuyên suốt các service:

```txt
Gateway ──→ Order Service ──→ Payment Service ──→ Notification
   2ms           15ms               30ms                5ms
   └────────────────────────────────────────────────────────┘
                     Total: 52ms
```

---

## 🛠 Yêu cầu môi trường

Để chạy dự án này trên máy của bạn, cần cài đặt sẵn:

| Công cụ | Phiên bản | Tải về |
| --- | --- | --- |
| **JDK** | 17 hoặc 21 | [Adoptium](https://adoptium.net/) |
| **Docker Desktop** | Latest | [Docker](https://www.docker.com/products/docker-desktop) |
| **PostgreSQL** | 15+ (qua Docker) | Docker image sẽ tự động pull |
| **Redis** | 7+ (qua Docker) | Docker image sẽ tự động pull |
| **DBeaver** | Latest | [DBeaver](https://dbeaver.io/) |
| **IntelliJ IDEA** | Community/Ultimate | [JetBrains](https://www.jetbrains.com/idea/download/) |
| **VS Code** | Latest | [VS Code](https://code.visualstudio.com/) (Extension: Java, Spring Boot) |
| **Apache Kafka** | 3.6+ (qua Docker) | Docker image sẽ tự động pull |

---

## 🚀 Hướng dẫn cài đặt và chạy dự án

### Bước 1: Clone dự án

```bash
git clone https://github.com/your-username/quickfood-platform.git
cd quickfood-platform
```

### Bước 2: Khởi động Hạ tầng (Database, Cache, Message Broker)

Hệ thống sử dụng Docker Compose để dựng toàn bộ hạ tầng (PostgreSQL, Redis, Kafka, Zookeeper).

```bash
docker-compose up -d
```

Lệnh này sẽ khởi động các container:

- **PostgreSQL** - Cơ sở dữ liệu chính
- **Redis** - Cache và lưu trữ realtime location
- **Zookeeper** - Quản lý cluster Kafka
- **Apache Kafka** - Message broker

### Bước 3: Kiểm tra kết nối Database

Sử dụng DBeaver hoặc công cụ PostgreSQL client:

| Parameter | Value |
| --- | --- |
| Host | `localhost` |
| Port | `5432` |
| Database | `quickfood_db` |
| User | `root` |
| Password | `rootpassword` |

### Bước 4: Khởi chạy các Microservices

Mở từng thư mục service và chạy lệnh Maven Wrapper (hoặc dùng Spring Boot Dashboard trong VS Code/IntelliJ).

> **Lưu ý:** Nên chạy các service theo thứ tự: Gateway → Auth → Các service còn lại.

#### 4.1. Chạy API Gateway (Port 8080)

```bash
cd api-gateway
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

API Gateway sẽ chạy ở địa chỉ: **http://localhost:8080**

#### 4.2. Chạy Auth Service (Port 8081)

```bash
cd auth-service
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

Auth Service sẽ chạy ở địa chỉ: **http://localhost:8081**

#### 4.3. Chạy các Service còn lại

Lặp lại tương tự cho các service:

- User Service (Port 8082)
- Restaurant Service (Port 8083)
- Menu Service (Port 8084)
- Order Service (Port 8085)
- Payment Service (Port 8086)
- Driver Service (Port 8087)
- Notification Service
- Review Service

> **💡 Tip:** Trong IntelliJ IDEA, bạn có thể cấu hình Run Configuration cho từng service hoặc dùng Spring Boot Dashboard trong VS Code để quản lý nhiều service cùng lúc.

### Bước 5: Kiểm tra hệ thống

Sau khi tất cả service đã chạy, kiểm tra:

```bash
# Kiểm tra API Gateway
curl http://localhost:8080/api/auth/health

# Đăng ký tài khoản
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","email":"test@example.com"}'

# Đăng nhập
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'
```

---

## 📁 Cấu trúc thư mục

```txt
quickfood-platform/
├── api-gateway/                    # Spring Cloud Gateway (Port 8080)
│   ├── src/main/java/.../apigateway/
│   │   ├── config/                # Gateway configuration
│   │   ├── filter/                # Custom filters (JWT, Logging, Rate Limit)
│   │   └── handler/               # Exception handlers
│   └── pom.xml
│
├── auth-service/                   # Auth Service (Port 8081)
│   ├── src/main/java/.../authservice/
│   │   ├── config/                # Security config
│   │   ├── controller/            # REST controllers
│   │   ├── dto/                   # Request/Response DTOs
│   │   ├── entity/                # JPA entities
│   │   ├── repository/            # Database repositories
│   │   ├── security/              # JWT, UserDetails
│   │   └── service/               # Business logic
│   └── pom.xml
│
├── user-service/                   # User Service (Port 8082)
│   ├── src/main/java/.../userservice/
│   └── pom.xml
│
├── restaurant-service/             # Restaurant Service (Port 8083)
│   ├── src/main/java/.../restaurantservice/
│   └── pom.xml
│
├── menu-service/                   # Menu Service (Port 8084)
│   ├── src/main/java/.../menuservice/
│   └── pom.xml
│
├── order-service/                  # Order Service (Port 8085)
│   ├── src/main/java/.../orderservice/
│   └── pom.xml
│
├── payment-service/                # Payment Service (Port 8086)
│   ├── src/main/java/.../paymentservice/
│   └── pom.xml
│
├── driver-service/                 # Driver Service (Port 8087)
│   ├── src/main/java/.../driverservice/
│   └── pom.xml
│
├── notification-service/           # Notification Service
│   ├── src/main/java/.../notificationservice/
│   └── pom.xml
│
├── review-service/                 # Review Service
│   ├── src/main/java/.../reviewservice/
│   └── pom.xml
│
├── location-service/               # Location Service
├── tracking-service/               # Tracking Service
│
├── docker-compose.yml              # Docker compose cho infrastructure
├── QuickFood_Microservice_Architecture.md  # Tài liệu kiến trúc chi tiết
└── README.md                       # Bạn đang đọc file này!
```

---

## 📡 API Endpoints

### Auth Service

| Method | Endpoint | Mô tả | Xác thực |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | Đăng ký | ❌ |
| POST | `/api/auth/login` | Đăng nhập | ❌ |
| POST | `/api/auth/refresh` | Refresh token | ❌ |
| POST | `/api/auth/forgot-password` | Quên mật khẩu | ❌ |
| POST | `/api/auth/logout` | Đăng xuất | ✅ |

### User Service

| Method | Endpoint | Mô tả | Xác thực |
| --- | --- | --- | --- |
| GET | `/api/users/profile` | Xem profile | ✅ |
| PUT | `/api/users/profile` | Sửa profile | ✅ |
| GET | `/api/users/addresses` | Danh sách địa chỉ | ✅ |
| POST | `/api/users/addresses` | Thêm địa chỉ | ✅ |
| DELETE | `/api/users/addresses/{id}` | Xóa địa chỉ | ✅ |

### Restaurant Service

| Method | Endpoint | Mô tả | Xác thực |
| --- | --- | --- | --- |
| GET | `/api/restaurants` | Danh sách nhà hàng | ❌ |
| GET | `/api/restaurants/{id}` | Chi tiết nhà hàng | ❌ |
| POST | `/api/restaurants` | Thêm nhà hàng | ✅ (ADMIN) |
| PUT | `/api/restaurants/{id}` | Sửa nhà hàng | ✅ (ADMIN) |

### Order Service

| Method | Endpoint | Mô tả | Xác thực |
| --- | --- | --- | --- |
| POST | `/api/orders` | Tạo đơn hàng | ✅ |
| GET | `/api/orders/{id}` | Chi tiết đơn hàng | ✅ |
| GET | `/api/orders/user` | Lịch sử đơn hàng | ✅ |
| PUT | `/api/orders/{id}/cancel` | Hủy đơn hàng | ✅ |
| GET | `/api/orders/{id}/status` | Trạng thái đơn hàng | ✅ |

### Payment Service

| Method | Endpoint | Mô tả | Xác thực |
| --- | --- | --- | --- |
| POST | `/api/payments` | Tạo thanh toán | ✅ |
| GET | `/api/payments/{id}` | Chi tiết thanh toán | ✅ |
| POST | `/api/payments/{id}/refund` | Hoàn tiền | ✅ (ADMIN) |

### Driver Service

| Method | Endpoint | Mô tả | Xác thực |
| --- | --- | --- | --- |
| GET | `/api/drivers/available` | Tài xế khả dụng | ❌ |
| PUT | `/api/drivers/status` | Online/Offline | ✅ (DRIVER) |
| POST | `/api/drivers/location` | Update vị trí | ✅ (DRIVER) |
| PUT | `/api/drivers/orders/{id}/accept` | Nhận đơn | ✅ (DRIVER) |
| PUT | `/api/drivers/orders/{id}/reject` | Từ chối đơn | ✅ (DRIVER) |

---

## 🛣 Lộ trình phát triển (Roadmap)

Dự án được chia thành 5 giai đoạn chính:

### ✅ Giai đoạn 1: Khởi tạo cấu trúc - API Gateway & Auth Service

- [x] Thiết lập cấu trúc dự án multi-module Maven

- [x] Xây dựng API Gateway (Spring Cloud Gateway)

- [x] Xây dựng Auth Service (JWT, Spring Security)

- [x] Thiết lập PostgreSQL và Docker Compose

- [x] Kết nối Database-per-service

### 🔄 Giai đoạn 2: Luồng Order - Payment (Đồng bộ)

- [ ] Xây dựng User Service

- [ ] Xây dựng Restaurant Service

- [ ] Xây dựng Menu Service (kết hợp Redis cache)

- [ ] Xây dựng Order Service (quản lý vòng đời đơn hàng)

- [ ] Xây dựng Payment Service (VNPay/MoMo/Stripe)

- [ ] Tích hợp luồng đặt hàng đồng bộ

### 📨 Giai đoạn 3: Event-Driven Architecture với Kafka

- [ ] Thiết lập Kafka cluster (Zookeeper + Broker)

- [ ] Xây dựng Kafka Producer/Consumer pattern

- [ ] Chuyển luồng Order-Payment sang bất đồng bộ

- [ ] Xây dựng Notification Service (Kafka Consumer)

- [ ] Thiết lập Dead Letter Queue (DLQ)

### 🚗 Giai đoạn 4: Realtime Tracking & Saga Pattern

- [ ] Xây dựng Driver Service

- [ ] Tích hợp Redis cho realtime location

- [ ] Xây dựng WebSocket server

- [ ] Xây dựng Location Service & Tracking Service

- [ ] Triển khai Saga Pattern (Choreography)

- [ ] Xử lý rollback distributed transaction

- [ ] Xây dựng Review Service

### 🚀 Giai đoạn 5: Docker hóa & Monitoring

- [ ] Đóng gói từng service thành Docker image

- [ ] Viết Docker Compose cho toàn bộ stack

- [ ] Thiết lập Prometheus metrics

- [ ] Xây dựng Grafana dashboard

- [ ] Tích hợp Zipkin distributed tracing

- [ ] Triển khai lên Kubernetes (Deployment, Service, Ingress, HPA)

- [ ] Load balancing với Nginx

---

## 🎓 Mục tiêu học tập

Dự án này được xây dựng với mục đích học tập và thực hành các công nghệ:

### Kiến trúc

- ✅ **Microservice Architecture** - Thiết kế và triển khai hệ thống microservices
- ✅ **Event-Driven Architecture** - Xử lý sự kiện bất đồng bộ với Kafka
- ✅ **Database-per-Service** - Mỗi service có database riêng

### Công nghệ

- ✅ **Spring Boot & Spring Cloud** - Framework phát triển microservices
- ✅ **Apache Kafka** - Message broker, event streaming
- ✅ **Redis** - Caching, realtime data, rate limiting
- ✅ **API Gateway** - Spring Cloud Gateway
- ✅ **JWT & OAuth** - Authentication & Authorization

### Patterns

- ✅ **Saga Pattern** - Distributed transaction management
- ✅ **CQRS** - Command Query Responsibility Segregation
- ✅ **Circuit Breaker** - Fault tolerance (Resilience4j)

### Hạ tầng

- ✅ **Docker & Docker Compose** - Containerization
- ✅ **Kubernetes** - Container orchestration
- ✅ **Prometheus & Grafana** - Monitoring
- ✅ **Zipkin** - Distributed tracing
- ✅ **Load Balancing** - Nginx

---

## 🤝 Đóng góp

Mọi đóng góp đều được chào đón! Vui lòng:

1. Fork dự án
2. Tạo branch feature (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Mở Pull Request

---

## 📄 Giấy phép

Dự án này được phát triển với mục đích học tập.

---

**⭐ Nếu bạn thấy dự án hữu ích, hãy cho nó một ngôi sao nhé! ⭐**