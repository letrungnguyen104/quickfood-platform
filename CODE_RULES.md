# 📐 QuickFood - Code Rules & Conventions

## 📋 Mục lục

- [Quy tắc đặt tên (Naming Conventions)](#-quy-tắc-đặt-tên-naming-conventions)
- [Cấu trúc Package chuẩn](#-cấu-trúc-package-chuẩn)
- [Luồng xử lý dữ liệu (Data Flow)](#-luồng-xử-lý-dữ-liệu-data-flow)
- [Chi tiết từng Package](#-chi-tiết-từng-package)
- [Quy tắc bổ sung](#-quy-tắc-bổ-sung)
- [Ví dụ mẫu hoàn chỉnh](#-ví-dụ-mẫu-hoàn-chỉnh)

---

## 📛 Quy tắc đặt tên (Naming Conventions)

### 1.1. Java

| Thành phần | Quy tắc | Ví dụ |
|-----------|---------|-------|
| **Class/Interface** | PascalCase | `OrderService`, `UserController`, `PaymentRepository` |
| **Method** | camelCase | `createOrder()`, `findByUserId()`, `cancelOrder()` |
| **Biến (Variable)** | camelCase | `orderId`, `userName`, `totalAmount` |
| **Hằng số (Constant)** | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE`, `JWT_SECRET` |
| **Package** | lowercase (không gạch dưới) | `com.quickfood.orderservice.dto.request` |
| **Enum** | PascalCase (class) + UPPER_SNAKE_CASE (values) | `OrderStatus.CREATED`, `PaymentMethod.VNPAY` |
| **Annotation** | PascalCase | `@RestController`, `@Service`, `@Entity` |
| **Generic Type** | Single uppercase letter | `<T>`, `<E>`, `<K, V>`, `<R>` |

### 1.2. Database

| Thành phần | Quy tắc | Ví dụ |
|-----------|---------|-------|
| **Table name** | snake_case (số nhiều) | `orders`, `order_items`, `user_roles` |
| **Column name** | snake_case | `created_at`, `updated_at`, `order_id` |
| **Primary key** | `id` (bigint/serial) | `id` |
| **Foreign key** | `{table}_id` | `order_id`, `user_id`, `restaurant_id` |
| **Index** | `idx_{table}_{column}` | `idx_orders_user_id`, `idx_orders_status` |
| **Unique constraint** | `uq_{table}_{column}` | `uq_users_email` |

### 1.3. API Endpoints

| Thành phần | Quy tắc | Ví dụ |
|-----------|---------|-------|
| **URL path** | lowercase, hyphen-separated | `/api/orders/{id}/cancel` |
| **Query params** | camelCase | `?pageSize=10&sortBy=createdAt` |
| **JSON fields** | camelCase | `"orderId": 123, "createdAt": "2024-01-15"` |

### 1.4. Git

| Thành phần | Quy tắc | Ví dụ |
|-----------|---------|-------|
| **Branch** | `{type}/{short-description}` | `feature/order-service`, `fix/payment-timeout`, `refactor/auth-service` |
| **Commit message** | `{type}: {message}` (tiếng Anh) | `feat: add order creation endpoint`, `fix: handle payment timeout` |

---

## 📁 Cấu trúc Package chuẩn

Mỗi microservice tuân theo cấu trúc package thống nhất:

```txt
com.quickfood.{service-name}/
│
├── config/                          # Cấu hình (Security, Kafka, Redis, ...)
│   ├── SecurityConfig.java
│   ├── KafkaConfig.java
│   ├── RedisConfig.java
│   └── WebSocketConfig.java
│
├── controller/                      # REST Controllers (tiếp nhận request)
│   ├── OrderController.java
│   └── PaymentController.java
│
├── dto/                             # Data Transfer Objects
│   ├── request/                     # Request DTOs (từ client gửi lên)
│   │   ├── CreateOrderRequest.java
│   │   └── UpdateOrderRequest.java
│   │
│   ├── response/                    # Response DTOs (trả về client)
│   │   ├── OrderResponse.java
│   │   └── PaymentResponse.java
│   │
│   └── common/                      # DTO dùng chung (shared)
│       ├── ApiResponse.java         # Response wrapper chuẩn
│       ├── PagedResponse.java       # Phân trang
│       ├── ErrorResponse.java       # Lỗi chuẩn
│       └── PageRequest.java         # Request phân trang
│
├── entity/                          # JPA Entities (ánh xạ DB)
│   ├── Order.java
│   └── OrderItem.java
│
├── enums/                           # Enum dùng chung
│   ├── OrderStatus.java
│   └── PaymentMethod.java
│
├── exception/                       # Custom Exceptions
│   ├── ResourceNotFoundException.java
│   ├── BusinessException.java
│   ├── UnauthorizedException.java
│   └── GlobalExceptionHandler.java  # Xử lý exception toàn cục
│
├── mapper/                          # MapStruct Mappers (Entity ↔ DTO)
│   ├── OrderMapper.java
│   └── OrderItemMapper.java
│
├── repository/                      # Spring Data JPA Repositories
│   ├── OrderRepository.java
│   └── OrderItemRepository.java
│
├── security/                        # Security (JWT, UserDetails, ...)
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
│
├── service/                         # Business Logic Layer
│   ├── OrderService.java
│   └── PaymentService.java
│
├── service/impl/                    # (Optional) Implementation classes
│   ├── OrderServiceImpl.java
│   └── PaymentServiceImpl.java
│
├── event/                           # Kafka Events
│   ├── producer/                    # Kafka Producers
│   │   ├── OrderEventProducer.java
│   │   └── PaymentEventProducer.java
│   │
│   └── consumer/                    # Kafka Consumers
│       ├── OrderEventConsumer.java
│       └── PaymentEventConsumer.java
│
├── constant/                        # Constants
│   ├── AppConstants.java
│   └── KafkaTopics.java
│
├── util/                            # Utility classes
│   ├── DateTimeUtils.java
│   └── JsonUtils.java
│
└── validator/                       # Custom validators
    ├── OrderValidator.java
    └── PhoneNumberValidator.java
```

---

## 🔄 Luồng xử lý dữ liệu (Data Flow)

### Luồng chuẩn: Client → Response

```txt
┌──────────┐     ┌────────────┐     ┌───────────┐     ┌──────────┐     ┌────────────┐
│  Client   │ ──► │ Controller │ ──► │  Service  │ ──► │ Mapper   │ ──► │ Repository │
│ (HTTP)    │     │ (REST)     │     │ (Business)│     │(Entity ↔ │     │   (JPA)    │
└──────────┘     └────────────┘     └───────────┘     │   DTO)   │     └─────┬──────┘
       ▲                            ┌───────────┐     └──────────┘           │
       │                            │ Exception │            │                │
       │                            │  Handler  │            │                ▼
       │                            └───────────┘            │          ┌────────────┐
       │                                  ▲                  │          │ Database   │
       │                                  │                  │          │ (PostgreSQL)│
       │                                  │                  ▼          └────────────┘
       │                                  │            ┌──────────┐
       └──────────────────────────────────┴────────────│  Entity  │
                                                        └──────────┘
```

### Luồng chi tiết từng bước

```txt
Bước 1: Client gửi HTTP Request
        └─► POST /api/orders
            Body: CreateOrderRequest (JSON)
            Header: Authorization: Bearer {JWT}

Bước 2: API Gateway nhận request
        └─► Xác thực JWT
        └─► Forward đến Order Service (Port 8085)

Bước 3: Controller nhận request
        └─► @Valid kiểm tra DTO
        └─► Gọi Service layer

Bước 4: Service xử lý business logic
        └─► Validate dữ liệu (Validator)
        └─► Mapper: DTO → Entity
        └─► Gọi Repository
        └─► Publish Kafka Event (nếu cần)

Bước 5: Repository tương tác DB
        └─► JPA save/find/delete
        └─► Trả về Entity

Bước 6: Service nhận Entity
        └─► Mapper: Entity → Response DTO
        └─► Trả về Controller

Bước 7: Controller trả về Response
        └─► ApiResponse.success(data)
        └─► HTTP 200 OK

      [Nếu có lỗi]
        └─► Exception Handler bắt lỗi
        └─► Trả về ErrorResponse
        └─► HTTP 4xx/5xx
```

---

## 📦 Chi tiết từng Package

### 2.1. `controller` - REST Controllers

**Vai trò:** Tiếp nhận HTTP request, kiểm tra đầu vào, gọi service, trả về response.

**Quy tắc:**
- Mỗi entity/resource có một Controller riêng
- Annotation `@RestController` + `@RequestMapping("/api/{resource}")`
- Method trả về `ResponseEntity<ApiResponse<T>>`
- Inject Service bằng constructor injection (`@RequiredArgsConstructor`)
- Không xử lý business logic trong Controller

```java
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id) {
        OrderResponse response = orderService.getOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
```

---

### 2.2. `dto/request` - Request DTOs

**Vai trò:** Nhận dữ liệu từ client gửi lên.

**Quy tắc:**
- Tên: `{Action}{Entity}Request` (ví dụ: `CreateOrderRequest`, `UpdateUserRequest`)
- Sử dụng `@NotBlank`, `@NotNull`, `@Size`, `@Pattern` từ Jakarta Validation
- Có thể dùng `@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)` nếu client gửi snake_case

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotEmpty(message = "Order must have at least one item")
    @Valid
    private List<OrderItemRequest> items;

    private String note;

    @NotNull(message = "Delivery address is required")
    private Long addressId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "Food ID is required")
    private Long foodId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
```

---

### 2.3. `dto/response` - Response DTOs

**Vai trò:** Trả dữ liệu về client.

**Quy tắc:**
- Tên: `{Entity}Response` (ví dụ: `OrderResponse`, `UserResponse`)
- Chỉ chứa các field cần thiết để trả về (không expose entity trực tiếp)
- Có thể dùng `@JsonInclude(JsonInclude.Include.NON_NULL)` để bỏ qua field null

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {

    private Long id;
    private String orderCode;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

---

### 2.4. `dto/common` - Common/Shared DTOs

**Vai trò:** Chứa các DTO dùng chung cho toàn bộ service.

**Gợi ý tên package:** `common` hoặc `shared` (khuyến nghị dùng `common` vì ngắn gọn và phổ biến).

```java
// ApiResponse.java - Response wrapper chuẩn (Generic Type)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    /**
     * Success response without data
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response with custom message
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response with data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Success response with custom message and data
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Error response
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

// PagedResponse.java - Phân trang
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

// ErrorResponse.java - Lỗi chuẩn
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int status;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors; // Lỗi validation chi tiết
}
```

---

### 2.5. `entity` - JPA Entities

**Vai trò:** Ánh xạ với bảng trong database.

**Quy tắc:**
- Tên class = tên bảng (PascalCase, số ít) - ví dụ: `Order` ↔ `orders` table
- Annotation `@Entity`, `@Table(name = "orders")`
- Sử dụng `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)` cho primary key
- Sử dụng `@Column(name = "column_name")` để map column
- Sử dụng `@CreatedDate`, `@LastModifiedDate` cho auditing
- Sử dụng `@PrePersist`, `@PreUpdate` cho tự động set thời gian
- **KHÔNG** expose entity ra ngoài Controller (luôn dùng DTO)

```java
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "note")
    private String note;

    @Column(name = "delivery_address_id", nullable = false)
    private Long deliveryAddressId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
```

---

### 2.6. `mapper` - MapStruct Mappers

**Vai trò:** Chuyển đổi giữa Entity và DTO.

**Quy tắc:**
- Sử dụng **MapStruct** (không dùng manual mapping)
- Tên: `{Entity}Mapper` (ví dụ: `OrderMapper`)
- Annotation `@Mapper(componentModel = "spring")`
- Method: `{Entity} toEntity({DTO})` và `{DTO} toDto({Entity})`

```java
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    Order toEntity(CreateOrderRequest request);

    OrderResponse toDto(Order order);

    List<OrderResponse> toDtoList(List<Order> orders);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Order order, UpdateOrderRequest request);
}
```

---

### 2.7. `exception` - Custom Exceptions

**Vai trò:** Định nghĩa các exception riêng và xử lý lỗi tập trung.

**Quy tắc:**
- Tạo các exception class riêng cho từng loại lỗi
- `GlobalExceptionHandler` với `@RestControllerAdvice` để bắt tất cả exception
- Trả về `ErrorResponse` chuẩn

```java
// ResourceNotFoundException.java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
    }
}

// BusinessException.java
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

// UnauthorizedException.java
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}

// GlobalExceptionHandler.java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Input validation failed")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .validationErrors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(
            Exception ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .path(request.getDescription(false).replace("uri=", ""))
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

### 2.8. `service` - Business Logic Layer

**Vai trò:** Xử lý toàn bộ business logic của ứng dụng.

**Quy tắc:**
- Annotation `@Service` + `@Transactional`
- Inject Repository và Mapper bằng constructor injection
- Không gọi trực tiếp Repository từ Controller
- Xử lý transaction: đọc dùng `@Transactional(readOnly = true)`, ghi dùng `@Transactional`
- Ném exception khi có lỗi business

```java
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer eventProducer;
    private final OrderValidator orderValidator;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // 1. Validate
        orderValidator.validateCreateOrder(request);

        // 2. Map DTO → Entity
        Order order = orderMapper.toEntity(request);
        order.setStatus(OrderStatus.CREATED);
        order.setOrderCode(generateOrderCode());

        // 3. Save to DB
        order = orderRepository.save(order);

        // 4. Publish Kafka Event
        eventProducer.publishOrderCreated(order);

        // 5. Return Response
        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return orderMapper.toDto(order);
    }

    private String generateOrderCode() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
```

---

### 2.9. `repository` - Spring Data JPA Repositories

**Vai trò:** Tương tác với database.

**Quy tắc:**
- Interface extends `JpaRepository<Entity, IdType>`
- Tên method tuân theo Spring Data JPA naming convention
- Sử dụng `@Query` cho các query phức tạp
- Sử dụng `@Modifying` + `@Query` cho update/delete operations

```java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Derived query methods
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<Order> findByOrderCode(String orderCode);

    List<Order> findByStatusAndRestaurantId(OrderStatus status, Long restaurantId);

    // Custom query
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId,
                                       @Param("status") OrderStatus status);

    // Pagination
    Page<Order> findByUserId(Long userId, Pageable pageable);

    // Count
    long countByStatus(OrderStatus status);

    // Update (Modifying)
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    int updateOrderStatus(@Param("id") Long id, @Param("status") OrderStatus status);
}
```

---

## 📌 Quy tắc bổ sung

### 3.1. Package bổ sung khuyến nghị

| Package | Mục đích | Ví dụ |
|---------|----------|-------|
| `config` | Cấu hình (Security, Kafka, Redis, WebSocket) | `SecurityConfig.java`, `KafkaConfig.java` |
| `constant` | Hằng số dùng chung | `AppConstants.java`, `KafkaTopics.java` |
| `enums` | Enum dùng chung | `OrderStatus.java`, `PaymentMethod.java` |
| `event/producer` | Kafka Event Producers | `OrderEventProducer.java` |
| `event/consumer` | Kafka Event Consumers | `OrderEventConsumer.java` |
| `security` | JWT, Filter, UserDetails | `JwtTokenProvider.java`, `JwtAuthFilter.java` |
| `util` | Utility/Helper classes | `DateTimeUtils.java`, `JsonUtils.java` |
| `validator` | Custom validation logic | `OrderValidator.java`, `PhoneValidator.java` |
| `filter` | Custom servlet filters | `RequestLoggingFilter.java` |
| `interceptor` | Interceptors | `RateLimitInterceptor.java` |

### 3.2. Quy tắc chung

#### Dependency Injection
- **Luôn dùng Constructor Injection** (qua `@RequiredArgsConstructor` từ Lombok)
- **KHÔNG dùng** `@Autowired` field injection

```java
// ✅ Đúng
@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
}

// ❌ Sai
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
}
```

#### Transaction Management
- `@Transactional(readOnly = true)` cho method chỉ đọc
- `@Transactional` cho method ghi (create/update/delete)
- Đặt transaction ở Service layer, không phải Controller

#### Logging
- Sử dụng SLF4J + Lombok `@Slf4j`
- Log ở mức `info` cho business flow chính
- Log ở mức `warn` cho các trường hợp đặc biệt
- Log ở mức `error` cho exception

```java
@Slf4j
@Service
public class OrderService {

    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for user: {}", request.getUserId());
        // ...
        log.info("Order created successfully: {}", order.getOrderCode());
        return response;
    }
}
```

#### Validation
- **DTO level:** Sử dụng Jakarta Validation annotations (`@NotBlank`, `@NotNull`, `@Size`, `@Pattern`)
- **Business level:** Sử dụng `Validator` class riêng cho logic phức tạp
- **Controller level:** `@Valid` hoặc `@Validated` trên request body

#### API Response Format
- Tất cả response đều wrap trong `ApiResponse<T>`
- Success: `ApiResponse.success(data)`
- Error: `ApiResponse.error(message)` hoặc `ErrorResponse`

#### Error Handling
- **Không try-catch** trong Controller (để GlobalExceptionHandler xử lý)
- Ném exception cụ thể: `ResourceNotFoundException`, `BusinessException`
- Validation lỗi → `MethodArgumentNotValidException` (Spring tự động)

#### Code Organization
- Một file = một class (trừ inner class nhỏ)
- Method tối đa 30 dòng
- Class tối đa 300 dòng
- Sử dụng Lombok để giảm boilerplate code (`@Data`, `@Builder`, `@Slf4j`, ...)

#### Testing
- Unit test cho Service layer
- Integration test cho Repository layer
- Controller test với MockMvc
- Tên test method: `{methodName}_{scenario}_{expectedResult}`

```java
@Test
void createOrder_validRequest_shouldReturnOrderResponse() {
    // Given
    CreateOrderRequest request = ...;
    // When
    OrderResponse response = orderService.createOrder(request);
    // Then
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(OrderStatus.CREATED);
}
```

---

## 🎯 Ví dụ mẫu hoàn chỉnh

### Luồng: Tạo đơn hàng mới

```txt
1. Client gửi POST /api/orders
   Body: CreateOrderRequest
   {
     "restaurantId": 1,
     "items": [{"foodId": 10, "quantity": 2}],
     "addressId": 5,
     "paymentMethod": "VNPAY",
     "note": "Giao giờ hành chính"
   }

2. OrderController.createOrder()
   ├── @Valid kiểm tra CreateOrderRequest
   └── Gọi orderService.createOrder(request)

3. OrderService.createOrder()
   ├── orderValidator.validateCreateOrder(request)
   ├── orderMapper.toEntity(request) → Order entity
   ├── order.setStatus(OrderStatus.CREATED)
   ├── order.setOrderCode("ORD-A1B2C3D4")
   ├── orderRepository.save(order) → DB
   ├── eventProducer.publishOrderCreated(order) → Kafka
   └── orderMapper.toDto(order) → OrderResponse

4. OrderController trả về
   Response: 200 OK
   {
     "success": true,
     "message": "Success",
     "data": {
       "id": 1,
       "orderCode": "ORD-A1B2C3D4",
       "status": "CREATED",
       "totalAmount": 150000,
       "paymentMethod": "VNPAY",
       "items": [...],
       "createdAt": "2024-01-15T10:30:00"
     },
     "timestamp": "2024-01-15T10:30:00"
   }
```

---

<div align="center">

**💡 Tuân thủ các quy tắc trên để đảm bảo codebase nhất quán, dễ đọc, dễ bảo trì!**

</div>