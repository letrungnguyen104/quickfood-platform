package com.quickfood.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    private String addressType;

    @NotBlank(message = "Receiver name must not be blank")
    private String receiverName;

    @NotBlank(message = "Receiver phone must not be blank")
    private String receiverPhone;

    @NotBlank(message = "Street address must not be blank")
    private String streetAddress;

    private Double latitude;
    private Double longitude;
    private boolean isDefault;
}