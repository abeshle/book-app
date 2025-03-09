package com.project.bookapp.controller;

import com.project.bookapp.dto.user.UserRegistrationRequestDto;
import com.project.bookapp.dto.user.UserResponseDto;
import com.project.bookapp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationRequestDto request) {
        return userService.register(request);
    }
}
