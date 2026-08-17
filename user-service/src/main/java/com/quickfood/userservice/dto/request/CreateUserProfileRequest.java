package com.quickfood.userservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserProfileRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid format")
    private String email;

    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    private String phoneNumber;
    private String avatarUrl;
}