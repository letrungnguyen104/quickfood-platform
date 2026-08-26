package com.quickfood.restaurantservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quickfood.restaurantservice.dto.common.ApiResponse;
import com.quickfood.restaurantservice.dto.request.CreateRestaurantRequest;
import com.quickfood.restaurantservice.dto.request.UpdateRestaurantRequest;
import com.quickfood.restaurantservice.dto.response.RestaurantResponse;
import com.quickfood.restaurantservice.service.RestaurantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant(@RequestBody CreateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.createRestaurant(request);
        return ResponseEntity.ok(ApiResponse.success("Restaurant created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurants() {
        List<RestaurantResponse> response = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getRestaurantById(@PathVariable Long id) {
        RestaurantResponse response = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.updateRestaurant(id, request);
        return ResponseEntity.ok(ApiResponse.success("Restaurant updated successfully", response));
    }

    @GetMapping("/my-restaurant")
    public ResponseEntity<ApiResponse<RestaurantResponse>> getMyRestaurant(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.success(restaurantService.getRestaurantByOwnerEmail(email)));
    }

}
