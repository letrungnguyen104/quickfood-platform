package com.quickfood.menuservice.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateMenuItemRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Boolean isAvailable;
}