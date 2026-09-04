package com.quickfood.orderservice.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MenuItemResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private boolean isAvailable;
}
