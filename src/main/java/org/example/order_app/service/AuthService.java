package org.example.order_app.service;

import org.example.order_app.dto.request.LoginRequestDTO;
import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.JwtResponseDTO;
import org.example.order_app.dto.response.UserResponseDTO;


public interface AuthService {
    UserResponseDTO registerUser(RegisterRequestDTO request);

    JwtResponseDTO login(LoginRequestDTO request);
}
