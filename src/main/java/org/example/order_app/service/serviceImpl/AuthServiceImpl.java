package org.example.order_app.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.Role;
import org.example.order_app.entity.User;
import org.example.order_app.exception.UserIsAlreadyTakenException;
import org.example.order_app.mapper.AuthMapper;
import org.example.order_app.mapper.UserMapper;
import org.example.order_app.repository.UserRepository;
import org.example.order_app.security.JwtUtils;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Сервис для управления аутентификацией и регистрацией пользователей.
 *
 * <p>Отвечает за:
 * <ul>
 *     <li>Регистрацию новых пользователей</li>
 *     <li>Аутентификацию пользователей</li>
 *     <li>Генерацию JWT токена</li>
 *     <li>Получение информации о текущем пользователе</li>
 * </ul>
 *
 * <p>Работает в рамках транзакции Spring (@Transactional).
 *
 * @see org.example.order_app.controller.AuthController
 * @see org.example.order_app.security.JwtUtils
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Регистрирует нового пользователя в системе.
     *
     * <p>Алгоритм работы:
     * <ol>
     *     <li>Проверяет, что username уникален</li>
     *     <li>Преобразует DTO в Entity</li>
     *     <li>Шифрует пароль</li>
     *     <li>Сохраняет пользователя в базе данных</li>
     * </ol>
     *
     * @param request DTO с регистрационными данными пользователя
     * @return DTO с сохранённым пользователем
     * @throws UserIsAlreadyTakenException если username уже занят
     */
    @Override
    public UserResponseDTO registerUser(RegisterRequestDTO request) {
        validateUsername(request.getUsername());
        User user = authMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    /**
     * Выполняет аутентификацию пользователя и генерирует JWT токен.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Передаёт credentials в AuthenticationManager</li>
     *     <li>При успешной аутентификации сохраняет Authentication в SecurityContext</li>
     *     <li>Генерирует JWT токен</li>
     *     <li>Возвращает токен и информацию о роли</li>
     * </ol>
     *
     * @param request DTO с логином и паролем
     * @return JWT токен и информация о пользователе
     */
    @Override
    public JwtResponseDTO login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER")
                .replace("ROLE_", "");

        return JwtResponseDTO.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .role(role)
                .build();
    }

    /**
     * Возвращает информацию о текущем аутентифицированном пользователе.
     *
     * @param userDetails данные пользователя из SecurityContext
     * @return DTO текущего пользователя
     * @throws ResponseStatusException если пользователь не аутентифицирован
     */
    @Override
    public UserResponseDTO getCurrentUser(UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String role = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("USER");

        return UserResponseDTO.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .role(Role.valueOf(role))
                .build();
    }

    /**
     * Проверяет уникальность имени пользователя.
     *
     * @param username имя пользователя
     * @throws UserIsAlreadyTakenException если пользователь уже существует
     */
    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UserIsAlreadyTakenException("Username is already taken" + username);
        }
    }
}
