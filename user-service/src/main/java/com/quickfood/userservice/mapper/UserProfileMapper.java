package com.quickfood.userservice.mapper;

import com.quickfood.userservice.dto.request.AddressRequest;
import com.quickfood.userservice.dto.request.CreateUserProfileRequest;
import com.quickfood.userservice.dto.request.UpdateUserProfileRequest;
import com.quickfood.userservice.dto.response.AddressResponse;
import com.quickfood.userservice.dto.response.UserProfileResponse;
import com.quickfood.userservice.entity.Address;
import com.quickfood.userservice.entity.UserProfile;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponse toUserProfileResponse(UserProfile entity);
    
    AddressResponse toAddressResponse(Address entity);
    
    List<AddressResponse> toAddressResponseList(List<Address> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loyaltyPoints", constant = "0")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserProfile toUserProfile(CreateUserProfileRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userProfile", ignore = true)
    Address toAddress(AddressRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "loyaltyPoints", ignore = true)
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserProfileFromDto(UpdateUserProfileRequest request, @MappingTarget UserProfile entity);
}