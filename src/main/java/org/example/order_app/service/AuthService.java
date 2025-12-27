package org.example.order_app.service;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.RegisterRequestDTO;
import org.example.order_app.entity.Role;
import org.example.order_app.entity.User;
import org.example.order_app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional //транзакция при ошибке откатывает все изменения. При успехе создает пользователя
    public User registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }
}
