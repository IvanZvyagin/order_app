package org.example.order_app.mapper;

import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    OrderResponseDTO toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "cratedAt", expression = "java(java.time.LocalDateTime.now())")
    Order toEntity(OrderRequestDTO requestDTO);
}
