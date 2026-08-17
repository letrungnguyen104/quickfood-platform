package com.quickfood.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private Long id;
    private String addressType;
    private String receiverName;
    private String receiverPhone;
    private String streetAddress;
    private Double latitude;
    private Double longitude;
    private boolean isDefault;
}