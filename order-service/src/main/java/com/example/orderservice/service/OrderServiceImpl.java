package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jpa.OrderEntity;
import com.example.orderservice.jpa.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper mapper;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        OrderEntity order = mapper.map(orderDto, OrderEntity.class);
        orderRepository.save(order);

        return mapper.map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        Iterable<OrderEntity> orderList = orderRepository.findByUserId(userId);

        List<OrderDto> returnValue = new ArrayList<>();
        orderList.forEach(
                order -> returnValue.add(mapper.map(order, OrderDto.class))
        );

        return returnValue;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity order = orderRepository.findByOrderId(orderId);
        return mapper.map(order, OrderDto.class);
    }
}
