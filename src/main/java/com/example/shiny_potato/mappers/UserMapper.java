package com.example.shiny_potato.mappers;

import com.example.shiny_potato.dto.UserDTO;
import com.example.shiny_potato.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getPassword(),
                user.getEmail(),
                user.getUserType().toString()
        );
    }
}
