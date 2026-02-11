package org.example.order_app.service;

import org.example.order_app.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для управления пользователями системы.
 *
 * <p>Предоставляет операции для:
 * <ul>
 *     <li>Получения списка всех пользователей</li>
 *     <li>Удаления пользователя по идентификатору</li>
 * </ul>
 *
 * <p>Как правило, доступ к данным операциям ограничен ролью ADMIN.
 */
public interface UserService {
    /**
     * Возвращает список всех пользователей системы.
     *
     * @return список пользователей в виде {@link UserResponseDTO}
     */
    List<UserResponseDTO> getAllUsers();

    /**
     * Удаляет пользователя по идентификатору
     *
     * @param id идентификатор пользователя для удаления.
     */
    void deleteUser(UUID id);
}
