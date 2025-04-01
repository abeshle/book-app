package com.project.bookapp.service;

import com.project.bookapp.dto.order.OrderRequestDto;
import com.project.bookapp.dto.order.OrderResponseDto;
import com.project.bookapp.dto.order.UpdateOrderStatusRequestDto;
import com.project.bookapp.dto.orderitem.OrderItemResponseDto;
import com.project.bookapp.model.User;
import java.util.List;

public interface OrderService {
    OrderResponseDto save(User user, OrderRequestDto orderRequestDto);

    List<OrderItemResponseDto> getOrderItems(Long id);

    OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    OrderResponseDto findById(Long id);

    List<OrderResponseDto> findAll(User user);
}
