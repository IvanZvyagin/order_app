package org.example.order_app.service;

import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.security.UserDetailsImpl;

/**
 * Сервис аутентификации и управления пользователями.
 *
 * <p>Отвечает за:
 * <ul>
 *     <li>Регистрацию пользователей</li>
 *     <li>Аутентификацию и генерацию JWT токена</li>
 *     <li>Получение данных текущего пользователя</li>
 * </ul>
 */
public interface AuthService {
    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param request DTO с данными регистрации
     * @return DTO зарегистрированного пользователя
     * @throws org.example.order_app.exception.UserIsAlreadyTakenException если имя пользователя уже занято
     */
    UserResponseDTO registerUser(RegisterRequestDTO request);

    /**
     * Выполняет аутентификацию пользователя и генерирует JWT токен.
     *
     * @param request DTO с логином и паролем
     * @return DTO, содержащий JWT токен и данные пользователя
     * @throws org.springframework.security.core.AuthenticationException если учетные данные неверны
     */
    JwtResponseDTO login(LoginRequestDTO request);

    /**
     * Возвращает информацию о текущем аутентифицированном пользователе.
     *
     * @param userDetails данные пользователя из SecurityContext
     * @return DTO с информацией о пользователе
     */
    UserResponseDTO getCurrentUser(UserDetailsImpl userDetails);

}
