package com.quickfood.menuservice.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
public class MenuItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private boolean isAvailable;
    private boolean isBestSeller;
}