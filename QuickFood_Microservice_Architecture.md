# QuickFood --- Distributed Food Delivery Platform

## 1. Tổng quan dự án

**Mục tiêu:** Xây dựng hệ thống giao đồ ăn dạng GrabFood/ShopeeFood với
kiến trúc microservice, hỗ trợ realtime tracking, event-driven
processing, cache, scaling, monitoring và fault tolerance.

Kiến trúc tổng thể:

``` txt
Frontend (React/Flutter)
        ↓
Nginx Load Balancer
        ↓
Spring Cloud Gateway
        ↓
-------------------------------------------------
Auth Service
User Service
Restaurant Service
Menu Service
Order Service
Driver Service
Payment Service
Notification Service
Location Service
Tracking Service
Review Service
-------------------------------------------------
        ↓
Kafka Event Bus
        ↓
Redis Cache
PostgreSQL (DB riêng từng service)
        ↓
Prometheus + Grafana + Zipkin
```

## 2. Functional Requirements

### Authentication

-   Đăng ký
-   Đăng nhập
-   Refresh token
-   Quên mật khẩu
-   OAuth login

### User Profile

-   Xem / sửa profile
-   Lưu địa chỉ
-   Lưu payment method

### Restaurant

-   Quản lý menu
-   Bật/tắt món
-   Chỉnh giá
-   Xem đơn

### Menu

Thông tin: - Tên món - Giá - Mô tả - Ảnh - Khuyến mãi - Trạng thái

### Order

Order states:

``` txt
CREATED
CONFIRMED
PREPARING
READY
DRIVER_ASSIGNED
PICKED_UP
DELIVERING
COMPLETED
CANCELLED
```

Chức năng: - Đặt món - Huỷ đơn - Theo dõi trạng thái - Xem lịch sử

### Driver

-   Online/offline
-   Nhận/từ chối đơn
-   Update vị trí
-   Hoàn thành giao hàng

### Payment

-   COD
-   VNPay
-   Momo
-   Stripe

### Notification

-   Driver assigned
-   Order delivered
-   Promotion
-   Push notification

### Review

-   Đánh giá tài xế
-   Đánh giá món
-   Đánh giá nhà hàng

------------------------------------------------------------------------

## 3. Non-functional Requirements

### Performance

-   1000 requests/s
-   500+ concurrent users

### Availability

-   SLA: 99.9%

### Scalability

Ví dụ:

``` txt
Order Service x3
Notification x5
```

### Fault tolerance

Payment chết → Order vẫn tồn tại → Saga rollback

### Consistency

Eventually consistent giữa các service

------------------------------------------------------------------------

## 4. Microservices

### Auth Service

DB: - users - roles - tokens

API:

``` txt
POST /login
POST /register
POST /refresh
```

JWT: - Access token - Refresh token

------------------------------------------------------------------------

### Restaurant Service

DB: - restaurants - categories

------------------------------------------------------------------------

### Menu Service

DB: - menus - foods

------------------------------------------------------------------------

### Order Service

DB: - orders - order_items

Kafka publish:

``` txt
OrderCreated
OrderCancelled
```

------------------------------------------------------------------------

### Driver Service

Redis:

``` txt
driver:location:{id}
```

------------------------------------------------------------------------

### Payment Service

Events:

``` txt
PaymentSuccess
PaymentFailed
```

------------------------------------------------------------------------

### Notification Service

Consume:

``` txt
OrderCreated
DriverAssigned
Delivered
```

## 5. Kafka Architecture

Flow:

``` txt
Order Service
   ↓
OrderCreated
   ↓
Kafka
   ↓
Payment
   ↓
PaymentSuccess
   ↓
Driver Service
   ↓
DriverAssigned
   ↓
Notification
```

Topics:

-   order-created
-   payment-success
-   payment-failed
-   driver-assigned
-   delivery-started
-   delivery-completed

DLQ:

``` txt
order-created-dlq
```

------------------------------------------------------------------------

## 6. Redis Usage

### Cache menu

``` txt
menu:{restaurantId}
TTL: 10 min
```

### Driver location

``` txt
driver:location:123
lat
lng
updatedAt
```

### ETA cache

### Rate limit API Gateway

``` txt
100 req/min
```

------------------------------------------------------------------------

## 7. API Gateway

Handle:

-   Authentication
-   Routing
-   Logging
-   Rate limiting

Ví dụ:

``` txt
/api/auth/*
→ Auth Service

/api/order/*
→ Order Service
```

------------------------------------------------------------------------

## 8. Realtime Tracking

Flow:

``` txt
Driver GPS
 ↓
Redis
 ↓
WebSocket
 ↓
Frontend
```

User thấy vị trí tài xế realtime.

------------------------------------------------------------------------

## 9. Distributed Transaction (Saga)

``` txt
Create order
 ↓
Payment
 ↓
Assign driver
 ↓
Notification
```

Nếu Payment fail:

``` txt
Cancel order
Refund
Rollback
```

------------------------------------------------------------------------

## 10. Monitoring

### Prometheus

-   CPU
-   Memory
-   Requests

### Grafana

Dashboard

### Zipkin

Distributed tracing:

``` txt
Gateway
→ Order
→ Payment
→ Notification
```

------------------------------------------------------------------------

## 11. Deployment

### Docker Compose

Deploy toàn bộ stack

### Kubernetes

-   Deployment
-   Service
-   Ingress
-   HPA

------------------------------------------------------------------------

## 12. Kiến trúc cuối cùng

``` txt
Client
 ↓
Load Balancer (Nginx)
 ↓
API Gateway
 ↓
Microservices
 ↓
Kafka
 ↓
Redis + PostgreSQL
 ↓
Monitoring
```

## Mục tiêu học được

-   Microservice architecture
-   Event-driven architecture
-   Kafka
-   Redis cache
-   API Gateway
-   Saga Pattern
-   Realtime system
-   Load balancing
-   Distributed tracing
-   Monitoring
-   Docker + Kubernetes
