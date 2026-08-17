package com.quickfood.userservice.dto.request;

import lombok.Data;

@Data
public class UpdateUserProfileRequest {
    private String fullName;
    private String phoneNumber;
    private String avatarUrl;
}