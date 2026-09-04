package com.quickfood.orderservice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.quickfood.orderservice.dto.request.OrderItemRequest;
import com.quickfood.orderservice.dto.request.OrderRequest;
import com.quickfood.orderservice.dto.response.OrderItemResponse;
import com.quickfood.orderservice.dto.response.OrderResponse;
import com.quickfood.orderservice.entity.Order;
import com.quickfood.orderservice.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse tOrderResponse(Order order);
    List<OrderResponse> toOrderResponseList(List<Order> orders);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toOrder(OrderRequest request);
    OrderItemResponse tOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "itemName", ignore = true)
    @Mapping(target = "price", ignore = true)
    OrderItem toOrderItem(OrderItemRequest request);
}
