package org.example.order_app.mapper;

import org.example.order_app.dto.request.RegisterRequestDTO;
import org.example.order_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "role", constant = "USER")
    User toEntity(RegisterRequestDTO requestDTO);
}
