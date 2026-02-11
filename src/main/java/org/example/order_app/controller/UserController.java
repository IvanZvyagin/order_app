package org.example.order_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер управления пользователями.
 * Доступен только с ролью ADMIN
 * <p>
 * Содержит эндпоинты для:
 * <ul>
 *   <li>Получения данных обо всех пользователях</li>
 *   <li>Удаления пользователя по id</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "API для управления пользователями")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final UserService userService;

    /**
     * Получение всех пользователей
     * @return информация о зарегистрированных пользователях
     */
    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Удаление пользователя
     * @param id предоставление id конкретного пользователя
     * @return информация об удалении пользователя
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователей", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
