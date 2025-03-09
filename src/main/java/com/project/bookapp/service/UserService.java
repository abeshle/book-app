package com.project.bookapp.service;

import com.project.bookapp.dto.user.UserRegistrationRequestDto;
import com.project.bookapp.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
