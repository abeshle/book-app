package com.project.bookapp.service;

import com.project.bookapp.dto.order.OrderRequestDto;
import com.project.bookapp.dto.order.OrderResponseDto;
import com.project.bookapp.dto.order.UpdateOrderStatusRequestDto;
import com.project.bookapp.dto.orderitem.OrderItemResponseDto;
import com.project.bookapp.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto save(User user, OrderRequestDto orderRequestDto);

    List<OrderItemResponseDto> getOrderItems(Long id);

    OrderResponseDto updateStatus(Long id, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    OrderResponseDto findById(Long id);

    Page<OrderResponseDto> findAll(User user, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
