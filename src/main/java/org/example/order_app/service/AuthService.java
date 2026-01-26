package org.example.order_app.service;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.User;
import org.example.order_app.exception.UserIsAlreadyTakenException;
import org.example.order_app.mapper.AuthMapper;
import org.example.order_app.mapper.UserMapper;
import org.example.order_app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;


    @Transactional
    public UserResponseDTO registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserIsAlreadyTakenException("Username is already taken" + request.getUsername());
        }
        User user = authMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
//        User user = User.builder()
//                .username(request.getUsername())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        return userRepository.save(user);
//   }
