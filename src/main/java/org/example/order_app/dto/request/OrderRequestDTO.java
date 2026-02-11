package org.example.order_app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
/**
 * DTO для создания нового заказа.
 * Используется в endpoint:
 * POST /api/orders
 * Содержит информацию, необходимую для создания заказа.
 */
@Data
public class OrderRequestDTO {
    /**
     * Описание заказа.
     * Должно содержать от 3 до 500 символов.
     */
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;
}
