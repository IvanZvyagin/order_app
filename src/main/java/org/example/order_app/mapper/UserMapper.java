package org.example.order_app.mapper;

import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDTO toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "orders", ignore = true)
    User toEntity(RegisterRequestDTO requestDTO);
}
