package ru.itmo.is.lab1.mapper;

import org.mapstruct.Mapper;
import ru.itmo.is.lab1.dto.UserDto;
import ru.itmo.is.lab1.model.AppUser;

@Mapper
public interface UserMapper {

    UserDto toDto(AppUser entity);
}
