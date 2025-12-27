package org.example.order_app.service;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.User;
import org.example.order_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }
    private UserResponseDTO convertToResponse(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
}
