package com.project.bookapp.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotEmpty
        @Size(max = 255)
        String email,

        @NotEmpty
        @Size(max = 255)
        String password
) {
}
