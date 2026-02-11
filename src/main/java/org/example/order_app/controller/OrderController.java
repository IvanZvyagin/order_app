package org.example.order_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.serviceImpl.OrderServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

/**
 * Контроллер для управления заказами.
 * <p>
 * Содержит эндпоинты для:
 * <ul>
 *   <li>Создания нового заказа</li>
 *   <li>Получения данных о заказах текущего пользователя(владельца заказов)</li>
 *   <li>Обновления статуса заказа (только для роли ADMIN)</li>
 *   <li>Удаления заказа (только для роли ADMIN или владельца заказа)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "API для управления заказами")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;

    /**
     * Создание нового заказа
     * @param request запрос на создание нового заказа с описанием
     * @param userDetails передача информации о пользователе
     * @return создание нового заказа с параметрами(id заказа, описание, статус, время создания, id пользователя и username)
     */
    @PostMapping
    @Operation(summary = "Создать заказ")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(orderServiceImpl.createOrder(request, userDetails.getId()));
    }

    /**
     *
     * @param userDetails информация о текущем пользователе
     * @param page начало страницы выдачи заказов
     * @param size максимальный размер выдачи
     * @return получение о заказах текущего пользователя
     */
    @GetMapping
    @Operation(summary = "Получить заказы текущего пользователя")
    public ResponseEntity<Page<OrderResponseDTO>> getOrderForCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(orderServiceImpl.getOrdersForCurrentUser(userDetails.getId(), pageable));
    }

    /**
     * Доступна только с ролью ADMIN
     * @param page начало страницы выдачи заказов
     * @param size максимальный размер выдачи заказов
     * @return получение информации обо всех заказов всех пользователей
     */
    @GetMapping("/all")
    @Operation(summary = "Получение всех заказов", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(orderServiceImpl.getAllOrders(pageable));
    }

    /**
     * Доступно только с ролью ADMIN
     * @param id запрос id конкретного заказа
     * @param request запрос на изменение статуса заказа
     * @return информация об изменении статуса заказа
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить статус заказа", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateRequest request) {
        OrderResponseDTO response = orderServiceImpl.updateOrderStatus(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     *
     * @param id предоставление id заказа
     * @param userDetails предоставление информации о пользователе, который удаляет заказ
     * @return информация об удалении заказа
     * @throws AccessDeniedException проверка на возможность удаления заказа(владелец заказа или админ)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ")
    public ResponseEntity<OrderResponseDTO> deleteOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        OrderResponseDTO response = orderServiceImpl.deleteOrder(id, userDetails);
        return ResponseEntity.ok(response);
    }
}