package com.quickfood.restaurantservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickfood.restaurantservice.dto.request.CreateRestaurantRequest;
import com.quickfood.restaurantservice.dto.request.UpdateRestaurantRequest;
import com.quickfood.restaurantservice.dto.response.RestaurantResponse;
import com.quickfood.restaurantservice.entity.Restaurant;
import com.quickfood.restaurantservice.exception.BusinessException;
import com.quickfood.restaurantservice.exception.ErrorCode;
import com.quickfood.restaurantservice.mapper.RestaurantMapper;
import com.quickfood.restaurantservice.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = restaurantMapper.toRestaurant(request);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurantMapper.toRestaurantResponseList(restaurants);
    }

    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));
        return restaurantMapper.toRestaurantResponse(restaurant);
    }

    @Transactional
    public RestaurantResponse updateRestaurant(Long id, UpdateRestaurantRequest request) {
        Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND));
        restaurantMapper.updateRestaurantFromDto(request, restaurant);
        Restaurant updated = restaurantRepository.save(restaurant);
        return restaurantMapper.toRestaurantResponse(updated);
    }

}
