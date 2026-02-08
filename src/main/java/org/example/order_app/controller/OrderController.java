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
 * Контроллер для управления заказами
 */
@RestController
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "API для управления заказами")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceImpl orderServiceImpl;


    @PostMapping
    @Operation(summary = "Создать заказ")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(orderServiceImpl.createOrder(request, userDetails.getId()));
    }

    @GetMapping
    @Operation(summary = "Получить заказы текущего пользователя")
    public ResponseEntity<Page<OrderResponseDTO>> getOrderForCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(orderServiceImpl.getOrdersForCurrentUser(userDetails.getId(), pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Получение всех заказов", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(orderServiceImpl.getAllOrders(pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить статус заказа", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateRequest request) {
        OrderResponseDTO response = orderServiceImpl.updateOrderStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ")
    public ResponseEntity<OrderResponseDTO> deleteOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        OrderResponseDTO response = orderServiceImpl.deleteOrder(id, userDetails);
        return ResponseEntity.ok(response);
    }
}
//todo Проверить работу контроллеров за Админа