package org.example.order_app.service;

import org.example.order_app.dto.response.UserResponseDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponseDTO> getAllUsers();

    void deleteUser(UUID id);
}
