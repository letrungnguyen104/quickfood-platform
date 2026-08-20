package com.quickfood.restaurantservice.dto.request;

import lombok.Data;
import java.time.LocalTime;

@Data
public class UpdateRestaurantRequest {
    private String name;
    private String description;
    private String coverImageUrl;
    private String address;
    private Double latitude;
    private Double longitude;
    private String contactPhone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String status;
}