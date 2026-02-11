package org.example.order_app.service.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.entity.Order;
import org.example.order_app.entity.User;
import org.example.order_app.mapper.OrderMapper;
import org.example.order_app.repository.OrderRepository;
import org.example.order_app.repository.UserRepository;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.UUID;
/**
 * Сервис для управления заказами пользователей.
 *
 * <p>Отвечает за:
 * <ul>
 *     <li>Создание заказа</li>
 *     <li>Получение заказов текущего пользователя</li>
 *     <li>Получение всех заказов (для ADMIN)</li>
 *     <li>Обновление статуса заказа</li>
 *     <li>Удаление заказа с проверкой прав доступа</li>
 * </ul>
 *
 * <p>Работает с {@link OrderRepository} и {@link UserRepository}
 * для взаимодействия с базой данных.
 *
 * @see org.example.order_app.controller.OrderController
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    /**
     * Создаёт новый заказ для указанного пользователя.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Проверяет существование пользователя</li>
     *     <li>Преобразует DTO в Entity</li>
     *     <li>Сохраняет заказ в базе данных</li>
     *     <li>Возвращает DTO сохранённого заказа</li>
     * </ol>
     *
     * @param request DTO с данными для создания заказа
     * @param uuid идентификатор пользователя
     * @return DTO созданного заказа
     * @throws EntityNotFoundException если пользователь не найден
     */
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request, UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found" + uuid));
        return orderMapper.toDto(orderRepository.save(orderMapper.toEntity(request, user)));
    }

    /**
     * Возвращает страницу заказов текущего пользователя.
     *
     * @param user идентификатор пользователя
     * @param pageable параметры пагинации и сортировки
     * @return страница заказов пользователя
     */
    @Override
    public Page<OrderResponseDTO> getOrdersForCurrentUser(UUID user, Pageable pageable) {
        return orderRepository.findAllByUser_Id(user, pageable)
                .map(orderMapper::toDto);
    }

    /**
     * Возвращает страницу всех заказов в системе.
     *
     * <p>Используется для административных операций.</p>
     *
     * @param pageable параметры пагинации
     * @return страница всех заказов
     */
    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    /**
     * Обновляет статус существующего заказа.
     *
     * <p>Алгоритм:
     * <ol>
     *     <li>Находит заказ по ID</li>
     *     <li>Обновляет статус через mapper</li>
     *     <li>Сохраняет изменения</li>
     * </ol>
     *
     * @param orderId идентификатор заказа
     * @param request DTO с новым статусом
     * @return DTO обновлённого заказа
     * @throws EntityNotFoundException если заказ не найден
     */
    @Override
    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден " + orderId));
        orderMapper.updateStatus(request, order);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }


    /**
     * Удаляет заказ при условии, что пользователь:
     * <ul>
     *     <li>Является владельцем заказа</li>
     *     <li>Или имеет роль ADMIN</li>
     * </ul>
     *
     * @param orderId идентификатор заказа
     * @param currentUser текущий аутентифицированный пользователь
     * @return DTO удалённого заказа
     * @throws EntityNotFoundException если заказ не найден
     * @throws AccessDeniedException если у пользователя нет прав на удаление
     */
    @Transactional
    @Override
    public OrderResponseDTO deleteOrder(UUID orderId, UserDetailsImpl currentUser) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        boolean isOwner = order.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this order");
        }
        orderRepository.delete(order);
        return orderMapper.toDto(order);
    }
}
