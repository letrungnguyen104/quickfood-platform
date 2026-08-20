package com.quickfood.restaurantservice.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.quickfood.restaurantservice.dto.request.CreateRestaurantRequest;
import com.quickfood.restaurantservice.dto.request.UpdateRestaurantRequest;
import com.quickfood.restaurantservice.dto.response.RestaurantResponse;
import com.quickfood.restaurantservice.entity.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    RestaurantResponse toRestaurantResponse(Restaurant restaurant);
    
    List<RestaurantResponse> toRestaurantResponseList(List<Restaurant> restaurants);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "averageRating", constant = "0.0")
    @Mapping(target = "totalReviews", constant = "0")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Restaurant toRestaurant(CreateRestaurantRequest createRestaurantRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "totalReviews", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateRestaurantFromDto(UpdateRestaurantRequest request, @MappingTarget Restaurant entity);

}