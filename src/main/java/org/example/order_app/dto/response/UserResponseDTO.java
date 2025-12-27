package org.example.order_app.dto.response;

import lombok.Builder;
import lombok.Data;
import org.example.order_app.entity.Role;

import java.util.UUID;

/**
 * ДТО ответа с данными юзера
 */
@Data
@Builder
public class UserResponseDTO {
    private UUID id;
    private String username;
    private Role role;
}
