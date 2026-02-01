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

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request, UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(()-> new EntityNotFoundException("User not found" + uuid));
        Order order = orderMapper.toEntity(request, user);
        Order saved = orderRepository.saveAndFlush(order);
        return orderMapper.toDto(saved);
    }

    @Override
    public Page<OrderResponseDTO> getOrdersForCurrentUser(UUID user, Pageable pageable) {
        return orderRepository.findAllByUser_Id(user, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Transactional
    @Override
    public OrderResponseDTO updateOrderStatus(UUID orderId, OrderUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ не найден " + orderId));
        orderMapper.updateStatus(request, order);
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

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
