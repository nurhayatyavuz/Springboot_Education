package com.example.specificationexmp.mapper;

import com.example.specificationexmp.dto.UserDto;
import com.example.specificationexmp.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    User toEntity(UserDto dto);
}


