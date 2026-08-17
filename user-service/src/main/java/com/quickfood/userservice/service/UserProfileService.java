package com.quickfood.userservice.service;

import com.quickfood.userservice.dto.request.AddressRequest;
import com.quickfood.userservice.dto.request.CreateUserProfileRequest;
import com.quickfood.userservice.dto.request.UpdateUserProfileRequest;
import com.quickfood.userservice.dto.response.AddressResponse;
import com.quickfood.userservice.dto.response.UserProfileResponse;
import com.quickfood.userservice.entity.Address;
import com.quickfood.userservice.entity.UserProfile;
import com.quickfood.userservice.exception.BusinessException;
import com.quickfood.userservice.exception.ErrorCode;
import com.quickfood.userservice.mapper.UserProfileMapper;
import com.quickfood.userservice.repository.AddressRepository;
import com.quickfood.userservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final AddressRepository addressRepository;
    private final UserProfileMapper userProfileMapper;

    @Transactional
    public UserProfileResponse createProfile(CreateUserProfileRequest request) {
        if (userProfileRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (request.getPhoneNumber() != null && userProfileRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_EXISTED);
        }

        UserProfile profile = userProfileMapper.toUserProfile(request);
        UserProfile saved = userProfileRepository.save(profile);
        return userProfileMapper.toUserProfileResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfileByEmail(String email) {
        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return userProfileMapper.toUserProfileResponse(profile);
    }

    @Transactional
    public UserProfileResponse updateProfile(String email, UpdateUserProfileRequest request) {
        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(profile.getPhoneNumber())) {
            if (userProfileRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new BusinessException(ErrorCode.PHONE_NUMBER_EXISTED);
            }
        }
        userProfileMapper.updateUserProfileFromDto(request, profile);

        UserProfile updated = userProfileRepository.save(profile);
        return userProfileMapper.toUserProfileResponse(updated);
    }

    @Transactional
    public AddressResponse addAddress(String email, AddressRequest request) {
        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (request.isDefault()) {
            profile.getAddresses().forEach(addr -> addr.setDefault(false));
        }

        Address address = userProfileMapper.toAddress(request);
        address.setUserProfile(profile);
        address.setDefault(request.isDefault() || profile.getAddresses().isEmpty());

        Address saved = addressRepository.save(address);
        return userProfileMapper.toAddressResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getUserAddresses(String email) {
        UserProfile profile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        return userProfileMapper.toAddressResponseList(profile.getAddresses());
    }
}