package org.example.order_app.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.mapper.UserMapper;
import org.example.order_app.repository.UserRepository;
import org.example.order_app.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

}
