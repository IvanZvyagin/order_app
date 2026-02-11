package org.example.order_app.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.mapper.UserMapper;
import org.example.order_app.repository.UserRepository;
import org.example.order_app.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для управления пользователями системы.
 *
 * <p>Предоставляет функциональность:
 * <ul>
 *     <li>Получение списка всех пользователей</li>
 *     <li>Удаление пользователя по идентификатору</li>
 * </ul>
 *
 * <p>Работает с {@link UserRepository} для доступа к данным
 * и {@link UserMapper} для преобразования Entity в DTO.
 *
 * @see org.example.order_app.controller.UserController
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Возвращает список всех пользователей системы.
     *
     * <p>Процесс:
     * <ol>
     *     <li>Получение всех пользователей из базы данных</li>
     *     <li>Преобразование сущностей {@code User} в {@code UserResponseDTO}</li>
     * </ol>
     *
     * @return список пользователей в формате DTO
     */
    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаляет пользователя по его идентификатору.
     *
     * <p>Операция выполняется в транзакции.</p>
     *
     * @param id уникальный идентификатор пользователя
     */
    @Transactional
    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

}
