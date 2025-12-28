package org.example.order_app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Почитать про 2auth аутентификацию, сколько должны хрангится токены, зачем нужен рефреш, как он приходит на бэк,
 * какой нужен эндпоинт
 * Access токен живет 10-15 минут, чтобы получить новый с помощью Refresh токена. Refresh токен хранится в БД
 * Подход к авторизации через JWT называется Stateless
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String username;
    private String role;
}
