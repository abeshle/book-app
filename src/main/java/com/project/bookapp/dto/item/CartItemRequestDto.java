package com.project.bookapp.dto.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long bookId;
    @NotNull
    @PositiveOrZero(message = "quantity must be non-negative")
    private int quantity;
}
