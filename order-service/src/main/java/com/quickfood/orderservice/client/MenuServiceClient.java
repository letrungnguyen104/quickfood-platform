package com.quickfood.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.quickfood.orderservice.dto.common.ApiResponse;
import com.quickfood.orderservice.dto.response.MenuItemResponse;

@FeignClient(name = "menu-service", url = "${app.services.menu-service.url}")
public interface MenuServiceClient {
    
    @GetMapping("/api/menus/items/{itemId}")
    ApiResponse<MenuItemResponse> getMenuItemById(@PathVariable("itemId") Long itemId);

}
