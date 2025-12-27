package org.example.order_app.dto;

import lombok.Builder;
import lombok.Data;
import org.example.order_app.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderResponseDTO {
    private UUID id;
    private String description;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private UUID userId;
    private String username;

}