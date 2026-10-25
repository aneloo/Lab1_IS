package ru.itmo.is.lab1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.is.lab1.dto.MagicCityDto;
import ru.itmo.is.lab1.model.MagicCity;

import java.util.List;

@Mapper(uses = TextMapper.class)
public interface MagicCityMapper {

    MagicCityDto toDto(MagicCity entity);

    List<MagicCityDto> toDtoList(List<MagicCity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", qualifiedByName = "trim")
    MagicCity toEntity(MagicCityDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", qualifiedByName = "trim")
    void update(MagicCityDto dto, @MappingTarget MagicCity entity);
}
