package org.example.order_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.order_app.entity.OrderStatus;

@Data
public class OrderUpdateRequest {
    @NotNull(message = "Статус обязателен")
    private OrderStatus status;
}
