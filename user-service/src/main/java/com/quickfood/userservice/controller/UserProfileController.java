package com.quickfood.userservice.controller;

import com.quickfood.userservice.dto.common.ApiResponse;
import com.quickfood.userservice.dto.request.AddressRequest;
import com.quickfood.userservice.dto.request.CreateUserProfileRequest;
import com.quickfood.userservice.dto.request.UpdateUserProfileRequest;
import com.quickfood.userservice.dto.response.AddressResponse;
import com.quickfood.userservice.dto.response.UserProfileResponse;
import com.quickfood.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> createProfile(@Valid @RequestBody CreateUserProfileRequest request) {
        UserProfileResponse response = userProfileService.createProfile(request);
        return ResponseEntity.ok(ApiResponse.success("Profile created successfully!", response));
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(@PathVariable String email) {
        UserProfileResponse response = userProfileService.getProfileByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @PathVariable String email,
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        UserProfileResponse response = userProfileService.updateProfile(email, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully!", response));
    }

    @PostMapping("/{email}/addresses")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @PathVariable String email,
            @Valid @RequestBody AddressRequest request
    ) {
        AddressResponse response = userProfileService.addAddress(email, request);
        return ResponseEntity.ok(ApiResponse.success("Address added successfully!", response));
    }

    @GetMapping("/{email}/addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddresses(@PathVariable String email) {
        List<AddressResponse> responses = userProfileService.getUserAddresses(email);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}