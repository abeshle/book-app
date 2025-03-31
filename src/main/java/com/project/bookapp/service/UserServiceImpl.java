package com.project.bookapp.service;

import com.project.bookapp.dto.user.UserRegistrationRequestDto;
import com.project.bookapp.dto.user.UserResponseDto;
import com.project.bookapp.exceptions.EntityNotFoundException;
import com.project.bookapp.exceptions.RegistrationException;
import com.project.bookapp.mapper.UserMapper;
import com.project.bookapp.model.Role;
import com.project.bookapp.model.RoleName;
import com.project.bookapp.model.User;
import com.project.bookapp.repository.role.RoleRepository;
import com.project.bookapp.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user");
        }
        Role roleUser = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Role ROLE_USER not found"));
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(roleUser));

        User savedUser = userRepository.save(user);
        shoppingCartService.createNewShoppingCart(savedUser);
        return userMapper.toDto(savedUser);
    }
}
