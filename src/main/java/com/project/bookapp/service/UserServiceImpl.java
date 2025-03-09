package com.project.bookapp.service;

import com.project.bookapp.dto.user.UserRegistrationRequestDto;
import com.project.bookapp.dto.user.UserResponseDto;
import com.project.bookapp.exceptions.RegistrationException;
import com.project.bookapp.mapper.UserMapper;
import com.project.bookapp.model.User;
import com.project.bookapp.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can't register user");
        }
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
}
