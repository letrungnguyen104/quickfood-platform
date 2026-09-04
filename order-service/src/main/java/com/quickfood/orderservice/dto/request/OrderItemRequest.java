package com.quickfood.orderservice.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull(message = "Menu item ID is required")
    private Long menuItemId;

    @NotNull(message = "quantity is required")
    private Integer quantity;
}
