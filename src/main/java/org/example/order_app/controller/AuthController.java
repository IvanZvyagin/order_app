package org.example.order_app.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.Role;
import org.example.order_app.mapper.UserMapper;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для аутентификации пользователя
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid
                                                @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid
                                                    @RequestBody RegisterRequestDTO registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        UserResponseDTO user = authService.getCurrentUser(userDetails);
//        return ResponseEntity.ok(authService.getCurrentUser(user));
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        String role = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("USER");

        return ResponseEntity.ok(UserResponseDTO.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .role(Role.valueOf(role))
                .build());
    }
}
