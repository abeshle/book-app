package com.project.bookapp.controller;

import com.project.bookapp.dto.cart.ShoppingCartResponseDto;
import com.project.bookapp.dto.item.CartItemRequestDto;
import com.project.bookapp.dto.item.CartItemResponseDto;
import com.project.bookapp.service.CartItemService;
import com.project.bookapp.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart management", description = "Endpoints for cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a user's shopping cart",
            description = "Get a user's shopping cart")
    public ShoppingCartResponseDto getById(@PathVariable Long id) {
        return shoppingCartService.findById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add an item to cart",
            description = "Add an item to cart")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addItemToCart(
            @RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.save(cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update item in cart",
            description = "Update item in cart")
    public CartItemResponseDto updateCartItem(@PathVariable Long cartItemId,
                                              @RequestBody CartItemRequestDto cartItemRequestDto) {
        return cartItemService.update(cartItemId, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete item from cart",
            description = "Delete item from cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        cartItemService.deleteById(cartItemId);
    }
}
