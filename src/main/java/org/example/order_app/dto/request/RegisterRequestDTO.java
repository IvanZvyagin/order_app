package org.example.order_app.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * DTO для регистрации нового пользователя.
 * Используется в endpoint:
 * POST /api/auth/register
 * Содержит данные, необходимые для создания учетной записи.
 */
@Data
public class RegisterRequestDTO {
    /**
     * Имя пользователя.
     * Должно содержать от 3 до 50 символов.
     */
    @NotBlank(message = "Username must be between 3 and 50 characters")
    @Size(min = 3, max = 50)
    private String username;
    /**
     * Пароль пользователя.
     * Должен содержать от 6 до 100 символов.
     */
    @NotBlank(message = "Password must be between 6 and 100 characters")
    @Size(min = 6, max = 100)
    private String password;
    /**
     * Подтверждение пароля.
     * Должен содержать от 6 до 100 символов.
     */
    @NotBlank(message = "Passwords don't match")
    @Size(min = 6, max = 100)
    private String confirmPassword;

    /**
     * Проверяет совпадение паролей.
     * @return true если password и confirmPassword совпадают.
     */
    @AssertTrue(message = "Password don't match")
    public boolean isPasswordsMatch(){
        if(password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }
}
