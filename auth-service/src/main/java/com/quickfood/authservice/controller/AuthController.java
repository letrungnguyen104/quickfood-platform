package com.quickfood.authservice.controller;

import com.quickfood.authservice.dto.common.ApiResponse;
import com.quickfood.authservice.dto.request.ForgotPasswordRequest;
import com.quickfood.authservice.dto.request.GoogleLoginRequest;
import com.quickfood.authservice.dto.request.LoginRequest;
import com.quickfood.authservice.dto.request.RefreshTokenRequest;
import com.quickfood.authservice.dto.request.RegisterRequest;
import com.quickfood.authservice.dto.request.ResendOtpRequest;
import com.quickfood.authservice.dto.request.ResetPasswordRequest;
import com.quickfood.authservice.dto.request.VerifyOtpRequest;
import com.quickfood.authservice.dto.response.AuthResponse;
import com.quickfood.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Account registered successfully!", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.success("Account verified successfully!", null));
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<Void>> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
        authService.resendOtp(request);
        return ResponseEntity.ok(ApiResponse.success("OTP resent successfully!", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful!", authResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        return ResponseEntity.ok(ApiResponse.success("Access granted!", "Current user is: " + currentUsername));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse authResponse = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully!", authResponse));
    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse<AuthResponse>> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        AuthResponse authResponse = authService.googleLogin(request);
        return ResponseEntity.ok(ApiResponse.success("Google login successful!", authResponse));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success("OTP sent to your email!", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully!", null));
    }

    @PutMapping("/upgrade-vendor/{email}")
    public ResponseEntity<ApiResponse<Void>> upgradeToVendor(@PathVariable String email) {
        authService.upgradeUserToVendor(email);
        return ResponseEntity.ok(ApiResponse.success("User upgraded to VENDOR successfully!", null));
    }

}