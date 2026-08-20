package com.quickfood.menuservice.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MenuItemRequest {
    @NotBlank(message = "Menu item name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    private BigDecimal price;
    
    private String imageUrl;
    private Boolean isAvailable;
}