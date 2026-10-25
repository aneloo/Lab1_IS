package ru.itmo.is.lab1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.is.lab1.dto.RingDto;
import ru.itmo.is.lab1.model.Ring;

import java.util.List;

@Mapper(uses = TextMapper.class)
public interface RingMapper {

    RingDto toDto(Ring entity);

    List<RingDto> toDtoList(List<Ring> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", qualifiedByName = "trim")
    Ring toEntity(RingDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", qualifiedByName = "trim")
    void update(RingDto dto, @MappingTarget Ring entity);
}
