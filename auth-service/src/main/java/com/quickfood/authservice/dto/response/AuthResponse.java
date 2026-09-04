package com.quickfood.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String refreshToken;
    @Builder.Default
    private String type = "Bearer";

    private UserInfo user;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private String email;
        private String username;
        private String fullName;
        private String avatarUrl;
        private String role;
    }

}