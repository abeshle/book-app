package com.project.bookapp.dto.cart;

import com.project.bookapp.dto.item.CartItemResponseDto;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
