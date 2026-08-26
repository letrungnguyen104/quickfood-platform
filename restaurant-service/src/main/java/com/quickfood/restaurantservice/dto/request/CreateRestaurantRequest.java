package com.quickfood.restaurantservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;

@Data
public class CreateRestaurantRequest {
    @NotBlank(message = "Restaurant name must not be blank")
    private String name;

    private String description;
    @NotBlank(message = "Owner email must not be blank")
    private String ownerEmail;
    private String coverImageUrl;

    @NotBlank(message = "Address must not be blank")
    private String address;

    private Double latitude;
    private Double longitude;
    private String contactPhone;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String deliveryTime;
    private Double deliveryFee;
    private Set<Long> tagIds;
}