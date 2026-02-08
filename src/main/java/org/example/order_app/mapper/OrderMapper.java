package org.example.order_app.mapper;

import org.example.order_app.dto.request.OrderRequestDTO;
import org.example.order_app.dto.request.OrderUpdateRequest;
import org.example.order_app.dto.response.OrderResponseDTO;
import org.example.order_app.entity.Order;
import org.example.order_app.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "description", target = "description")
    OrderResponseDTO toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Order toEntity(OrderRequestDTO requestDTO, User user);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateStatus(OrderUpdateRequest requestDTO, @MappingTarget Order order);
}
