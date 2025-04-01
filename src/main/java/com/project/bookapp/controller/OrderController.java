package com.project.bookapp.controller;

import com.project.bookapp.dto.order.OrderRequestDto;
import com.project.bookapp.dto.order.OrderResponseDto;
import com.project.bookapp.dto.order.UpdateOrderStatusRequestDto;
import com.project.bookapp.dto.orderitem.OrderItemResponseDto;
import com.project.bookapp.model.User;
import com.project.bookapp.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management", description = "Endpoints for orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get a user's order history",
            description = "Retrieve the list of orders placed by a user")
    public Page<OrderResponseDto> getUserOrders(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAll(user,pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Place an order",
            description = "Place a new order by a user from the shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto placeOrder(
            Authentication authentication,
            @RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.save(user, orderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}")
    @Operation(summary = "Get a specific order",
            description = "Show details of a specific order by its ID")
    public OrderResponseDto getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findById(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all items in a specific order",
            description = "Show all items that are part of a specific order")
    public List<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Update the status of a specific order")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        return orderService.updateStatus(orderId, updateOrderStatusRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get a specific order item",
            description = "Retrieve a specific OrderItem within an order")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }
}
