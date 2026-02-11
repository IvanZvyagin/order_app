package org.example.order_app.service;

import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

/**
 * Сервис для управления заказами.
 *
 * <p>Предоставляет бизнес-логику для:
 * <ul>
 *     <li>Создания заказов</li>
 *     <li>Получения заказов пользователя</li>
 *     <li>Получения всех заказов (для администратора)</li>
 *     <li>Обновления статуса заказа</li>
 *     <li>Удаления заказа с проверкой прав доступа</li>
 * </ul>
 */
public interface OrderService {
    /**
     * Создаёт новый заказ для указанного пользователя.
     *
     * @param request DTO с данными заказа
     * @param uuid    идентификатор пользователя, создающего заказ
     * @return DTO созданного заказа
     * @throws jakarta.persistence.EntityNotFoundException если пользователь не найден
     */
    OrderResponseDTO createOrder(OrderRequestDTO request, UUID uuid);

    /**
     * Возвращает страницу заказов текущего пользователя.
     *
     * @param user     идентификатор пользователя
     * @param pageable параметры пагинации и сортировки
     * @return страница заказов пользователя
     */
    Page<OrderResponseDTO> getOrdersForCurrentUser(UUID user, Pageable pageable);

    /**
     * Удаляет заказ при наличии прав доступа.
     *
     * <p>Удаление разрешено:
     * <ul>
     *     <li>Владельцу заказа</li>
     *     <li>Пользователю с ролью ADMIN</li>
     * </ul>
     *
     * @param orderId     идентификатор заказа
     * @param currentUser текущий аутентифицированный пользователь
     * @return DTO удалённого заказа
     * @throws jakarta.persistence.EntityNotFoundException если заказ не найден
     * @throws AccessDeniedException                       если у пользователя недостаточно прав
     */
    OrderResponseDTO deleteOrder(UUID orderId, UserDetailsImpl currentUser) throws AccessDeniedException;

    /**
     * Возвращает страницу всех заказов в системе.
     *
     * <p>Метод предназначен для использования администраторами.
     *
     * @param pageable параметры пагинации и сортировки
     * @return страница всех заказов
     */
    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

    /**
     * Обновляет статус заказа.
     *
     * @param orderId идентификатор заказа
     * @param request DTO с новым статусом
     * @return DTO обновлённого заказа
     * @throws jakarta.persistence.EntityNotFoundException если заказ не найден
     */
    OrderResponseDTO updateOrderStatus(UUID orderId, OrderUpdateRequest request);
}
