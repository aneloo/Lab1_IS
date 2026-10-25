package ru.itmo.is.lab1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.is.lab1.dto.CoordinatesDto;
import ru.itmo.is.lab1.model.Coordinates;

import java.util.List;

@Mapper
public interface CoordinatesMapper {

    CoordinatesDto toDto(Coordinates entity);

    List<CoordinatesDto> toDtoList(List<Coordinates> entities);

    @Mapping(target = "id", ignore = true)
    Coordinates toEntity(CoordinatesDto dto);

    @Mapping(target = "id", ignore = true)
    void update(CoordinatesDto dto, @MappingTarget Coordinates entity);
}
