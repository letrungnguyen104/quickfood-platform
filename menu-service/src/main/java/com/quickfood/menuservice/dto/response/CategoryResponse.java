package com.quickfood.menuservice.dto.response;

import java.util.List;

import lombok.*;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private Long restaurantId;
    private String name;
    private String description;
    private Integer sortOrder;
    private List<MenuItemResponse> menuItems; 
}