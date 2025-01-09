package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);

    List<OrderDto> getOrdersByUserId(String userId);

    OrderDto getOrderByOrderId(String orderId);
}
