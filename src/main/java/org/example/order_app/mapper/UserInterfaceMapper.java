package org.example.order_app.mapper;

import org.example.order_app.dto.UserDTO;
import org.mapstruct.Mapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper(componentModel = "spring")
public interface UserInterfaceMapper {
    UserDTO toClientResponseAdmin(User user);
}
