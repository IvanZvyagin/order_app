package org.example.order_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.error.ApiError;
import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Контроллер аутентификации и получения данных текущего пользователя.
 * <p>
 * Содержит эндпоинты для:
 * <ul>
 *   <li>регистрации</li>
 *   <li>логина (JWT)</li>
 *   <li>получение данных о пользователе /me</li>
 * </ul>
 */

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Аутентификация пользователя по логину и паролю.
     *
     * @param loginRequest запрос с username/password
     * @return JWT токен и базовая информация (роль, username)
     */

    @PostMapping("/login")
    @Operation(summary = "Авторизация пользователя")
    public ResponseEntity<JwtResponseDTO> login(@Valid
                                                @RequestBody LoginRequestDTO loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    /**
     * Регистрация пользователя по логину, паролю и подтверждению пароля
     * @param registerRequest запрос с username/password/confirmPassword
     * @return информация о зарегистрированном пользователе (id, username, роль)
     */
    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя")
    public ResponseEntity<UserResponseDTO> register(@Valid
                                                    @RequestBody RegisterRequestDTO registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest));
    }

    /**
     * Получение данных об аутентифицированном пользователе
     * @param userDetails предоставление данных об аутентифицированном пользователе
     * @return информация о пользователе (username, JWT токен, роль)
     */
    @GetMapping("/me")
    @Operation(summary = "Получение данных о пользователе")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(authService.getCurrentUser(userDetails));
    }
}
