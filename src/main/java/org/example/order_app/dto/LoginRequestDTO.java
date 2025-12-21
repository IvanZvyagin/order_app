package org.example.order_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
