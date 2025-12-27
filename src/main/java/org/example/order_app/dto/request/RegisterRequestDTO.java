package org.example.order_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotBlank(message = "Username must be between 3 and 50 characters")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password must be between 6 and 100 characters")
    @Size(min = 6, max = 100)
    private String password;

    @NotBlank(message = "Passwords don't match")
    @Size(min = 6, max = 100)
    private String confirmPassword;
}
