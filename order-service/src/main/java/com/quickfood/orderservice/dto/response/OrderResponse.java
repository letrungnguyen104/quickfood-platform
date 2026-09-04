package com.quickfood.orderservice.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.quickfood.orderservice.enums.OrderStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String customerEmail;
    private Long restaurantId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String deliveryAddress;
    private String customerPhone;
    private String notes;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;
}
