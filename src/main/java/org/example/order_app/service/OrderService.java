package org.example.order_app.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.entity.Order;
import org.example.order_app.entity.OrderStatus;
import org.example.order_app.entity.Role;
import org.example.order_app.entity.User;
import org.example.order_app.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request, User user){
        Order order = Order.builder()
                .description(request.getDescription())
                .status(OrderStatus.CREATED)
                .user(user)
                .cratedAt(LocalDateTime.now())
                .build();
        Order saveOrder = orderRepository.save(order);

        return convertToResponse(saveOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getOrdersForCurrentUser(User user, Pageable pageable){
        Page<Order> orders = orderRepository.findAllByUser(user, pageable);
        return orders.map(this::convertToResponse);
    }

    @Transactional
    public void deleteOrder(UUID orderId, User currentUser) throws AccessDeniedException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        boolean isOwner = order.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You don't have permission to delete this order");
        }
            orderRepository.delete(order);
    }

    @Transactional
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable){
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(this::convertToResponse);
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderUpdateRequest request){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new EntityNotFoundException("Заказ не найден " + orderId));
        order.setStatus(request.getStatus());
        Order updateOrder = orderRepository.save(order);
            return convertToResponse(updateOrder);
    }

    private OrderResponseDTO convertToResponse(Order order){
        return OrderResponseDTO.builder()
                .id(order.getId())
                .description(order.getDescription())
                .status(order.getStatus())
                .createdAt(order.getCratedAt())
                .userId(order.getUser().getId())
                .username(order.getUser().getUsername())
                .build();
    }
}
