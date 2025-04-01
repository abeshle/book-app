package com.project.bookapp.service;

import com.project.bookapp.dto.order.OrderRequestDto;
import com.project.bookapp.dto.order.OrderResponseDto;
import com.project.bookapp.dto.order.UpdateOrderStatusRequestDto;
import com.project.bookapp.dto.orderitem.OrderItemResponseDto;
import com.project.bookapp.exceptions.DataProcessingException;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.mapper.OrderItemMapper;
import com.project.bookapp.mapper.OrderMapper;
import com.project.bookapp.model.Order;
import com.project.bookapp.model.OrderItem;
import com.project.bookapp.model.ShoppingCart;
import com.project.bookapp.model.Status;
import com.project.bookapp.model.User;
import com.project.bookapp.repository.cart.ShoppingCartRepository;
import com.project.bookapp.repository.order.OrderRepository;
import com.project.bookapp.repository.user.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto save(User user, OrderRequestDto orderRequestDto) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found with ID: " + user.getId()));

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Cannot place an order with an empty cart");
        }

        Order order = new Order();
        order.setUser(existingUser);
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);

        List<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .toList();

        order.setOrderItems(new HashSet<>(orderItems));

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateStatus(
            Long id,
            UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with ID: " + id));
        order.setStatus(updateOrderStatusRequestDto.getStatus());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Order not found with ID: " + id));
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> findAll(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }
}
