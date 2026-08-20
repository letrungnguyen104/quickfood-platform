package com.quickfood.menuservice.controller;

import com.quickfood.menuservice.dto.common.ApiResponse;
import com.quickfood.menuservice.dto.request.CategoryRequest;
import com.quickfood.menuservice.dto.request.MenuItemRequest;
import com.quickfood.menuservice.dto.request.UpdateMenuItemRequest;
import com.quickfood.menuservice.dto.response.CategoryResponse;
import com.quickfood.menuservice.dto.response.MenuItemResponse;
import com.quickfood.menuservice.service.CategoryService;
import com.quickfood.menuservice.service.MenuItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final CategoryService categoryService;
    private final MenuItemService menuItemService;

    // --- CATEGORY APIs ---
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.createCategory(request)));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMenuByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getMenuByRestaurant(restaurantId)));
    }

    // --- MENU ITEM APIs ---
    @PostMapping("/categories/{categoryId}/items")
    public ResponseEntity<ApiResponse<MenuItemResponse>> addMenuItem(
            @PathVariable Long categoryId,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.addMenuItem(categoryId, request)));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.updateMenuItem(itemId, request)));
    }
}