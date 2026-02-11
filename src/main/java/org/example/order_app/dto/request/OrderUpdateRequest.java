package org.example.order_app.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.order_app.entity.OrderStatus;
/**
 * DTO для обновления статуса заказа.
 * Используется в endpoint:
 * PUT /api/orders/{id}
 * Требует роль ADMIN.
 */
@Data
public class OrderUpdateRequest {
    /**
     * Новый статус заказа.
     * Обязательное поле.
     */
    @NotNull(message = "Статус обязателен")
    private OrderStatus status;
}
