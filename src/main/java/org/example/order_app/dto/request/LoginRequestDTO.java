package org.example.order_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * DTO для запроса аутентификации пользователя.
 * Используется в endpoint:
 * POST /api/auth/login
 * Содержит учетные данные пользователя для получения JWT токена.
 */
@Data
public class LoginRequestDTO {
    /**
     * Уникальное имя пользователя.
     * Не может быть пустым.
     */
    @NotBlank(message = "Username is required")
    private String username;
    /**
     * Пароль пользователя.
     * Не может быть пустым.
     */
    @NotBlank(message = "Password is required")
    private String password;

}
