package com.quickfood.restaurantservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse {
    private Long id;
    private String name;
    private String description;
    private String coverImageUrl;
    private String address;
    private Double latitude;
    private Double longitude;
    private String contactPhone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double averageRating;
    private Integer totalReviews;
    private String status;
    private LocalDateTime createdAt;
}