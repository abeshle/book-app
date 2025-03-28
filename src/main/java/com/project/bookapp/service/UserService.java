package com.project.bookapp.service;

import com.project.bookapp.dto.user.UserRegistrationRequestDto;
import com.project.bookapp.dto.user.UserResponseDto;
import com.project.bookapp.exceptions.RegistrationException;
import com.project.bookapp.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    User getUser();
}
