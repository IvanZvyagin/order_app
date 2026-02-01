package org.example.order_app.service;

import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.security.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request, UUID uuid);

    Page<OrderResponseDTO> getOrdersForCurrentUser(UUID user, Pageable pageable);

    OrderResponseDTO deleteOrder(UUID orderId, UserDetailsImpl currentUser) throws AccessDeniedException;

    Page<OrderResponseDTO> getAllOrders(Pageable pageable);

    OrderResponseDTO updateOrderStatus(UUID orderId, OrderUpdateRequest request);
}
