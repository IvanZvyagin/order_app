package org.example.order_app.mapper;

import org.example.order_app.dto.response.UserResponseDTO;
import org.example.order_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponseDTO toDto(User user);

    @Mapping(target = "password", ignore = true)
    User toEntity(UserResponseDTO responseDTO);
}
