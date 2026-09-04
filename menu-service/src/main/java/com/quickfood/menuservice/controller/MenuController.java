package com.quickfood.menuservice.controller;

import com.quickfood.menuservice.dto.common.ApiResponse;
import com.quickfood.menuservice.dto.request.CreateCategoryRequest;
import com.quickfood.menuservice.dto.request.UpdateCategoryRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final CategoryService categoryService;
    private final MenuItemService menuItemService;

    // --- CATEGORY APIs ---
    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.createCategory(request)));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getMenuByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getMenuByRestaurant(restaurantId)));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.updateCategory(id, request)));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully!", null));
    }

    // --- MENU ITEM APIs ---
    @PostMapping("/categories/{categoryId}/items")
    public ResponseEntity<ApiResponse<MenuItemResponse>> addMenuItem(
            @PathVariable Long categoryId,
            @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.addMenuItem(categoryId, request)));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getMenuItem(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.getMenuItemById(id)));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(menuItemService.updateMenuItem(itemId, request)));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long itemId) {
        menuItemService.deleteMenuItem(itemId);
        return ResponseEntity.ok(ApiResponse.success("Item deleted successfully!", null));
    }
}