package com.quickfood.restaurantservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CreateRestaurantRequest {
    @NotBlank(message = "Restaurant name must not be blank")
    private String name;

    private String description;
    private String coverImageUrl;

    @NotBlank(message = "Address must not be blank")
    private String address;

    private Double latitude;
    private Double longitude;
    private String contactPhone;
    private LocalTime openTime;
    private LocalTime closeTime;
}