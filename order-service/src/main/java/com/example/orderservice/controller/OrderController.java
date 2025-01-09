package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.RequestOrder;
import com.example.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-service")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper mapper;

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId,
                                                     @RequestBody RequestOrder orderDetails) {
        OrderDto requestDto = mapper.map(orderDetails, OrderDto.class);
        requestDto.setUserId(userId);

        OrderDto responseDto = orderService.createOrder(requestDto);

        ResponseOrder responseOrder = mapper.map(responseDto, ResponseOrder.class);
        return ResponseEntity.created(null)
                             .body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        List<OrderDto> orderList = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = orderList.stream()
                                              .map(order -> mapper.map(order, ResponseOrder.class))
                                              .toList();

        return ResponseEntity.ok(result);
    }
}
