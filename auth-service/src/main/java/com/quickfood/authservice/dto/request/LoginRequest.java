package com.quickfood.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username or Email must not be blank")
    private String identifier;

    @NotBlank(message = "Password must not be blank")
    private String password;
}