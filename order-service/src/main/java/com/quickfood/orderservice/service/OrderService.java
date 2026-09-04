package com.quickfood.orderservice.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickfood.orderservice.client.MenuServiceClient;
import com.quickfood.orderservice.dto.request.OrderItemRequest;
import com.quickfood.orderservice.dto.request.OrderRequest;
import com.quickfood.orderservice.dto.response.MenuItemResponse;
import com.quickfood.orderservice.dto.response.OrderResponse;
import com.quickfood.orderservice.entity.Order;
import com.quickfood.orderservice.entity.OrderItem;
import com.quickfood.orderservice.enums.OrderStatus;
import com.quickfood.orderservice.exception.BusinessException;
import com.quickfood.orderservice.exception.ErrorCode;
import com.quickfood.orderservice.mapper.OrderMapper;
import com.quickfood.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MenuServiceClient menuServiceClient;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = orderMapper.toOrder(request);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        if(request.getItems() != null) {
            for(OrderItemRequest itemReq : request.getItems()) {
                MenuItemResponse item = menuServiceClient.getMenuItemById(itemReq.getMenuItemId()).getData();
                if(!item.isAvailable()) {
                    throw new BusinessException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Item " + item.getName() + " is currently sold out!");
                }
                OrderItem orderItem = orderMapper.toOrderItem(itemReq);
                orderItem.setItemName(item.getName());
                orderItem.setPrice(item.getPrice());
                BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                totalAmount = totalAmount.add(itemTotal);
                order.addOrderItem(orderItem);
            }
        }

        totalAmount = totalAmount.add(new BigDecimal("1.50"));
        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.tOrderResponse(savedOrder);

    }
}
