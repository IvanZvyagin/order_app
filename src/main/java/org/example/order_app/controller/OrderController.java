package org.example.order_app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.entity.Role;
import org.example.order_app.entity.User;
import org.example.order_app.security.UserDetailsImpl;
import org.example.order_app.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "API для управления заказами")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Создать заказ")
    public ResponseEntity<OrderResponseDTO> createOrder(
            @Valid @RequestBody OrderRequestDTO request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = User.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .build();
        OrderResponseDTO response = orderService.createOrder(request, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Получить заказы текущего пользователя")
    public ResponseEntity<Page<OrderResponseDTO>> getOrderForCurrentUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Parameter(description = "Параметры пагинации")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        User user = User.builder()
                .id(userDetails.getId())
                .build();
        Page<OrderResponseDTO> orders = orderService.getOrdersForCurrentUser(user, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/all")
    @Operation(summary = "Получение всех заказов", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<OrderResponseDTO> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить статус заказа", description = "Требуется роль ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderUpdateRequest request) {
        OrderResponseDTO response = orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws AccessDeniedException {
        User user = User.builder()
                .id(userDetails.getId())
                .role(Role.valueOf(userDetails.getAuthorities().iterator()
                        .next().getAuthority().replace("ROLE_", "")))
                .build();
        orderService.deleteOrder(id, user);
        return ResponseEntity.noContent().build();
    }
}
