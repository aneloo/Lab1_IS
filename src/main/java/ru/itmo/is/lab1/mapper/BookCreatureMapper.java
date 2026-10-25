package ru.itmo.is.lab1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.model.BookCreature;

import java.util.List;

@Mapper(uses = {CoordinatesMapper.class, MagicCityMapper.class, RingMapper.class, TextMapper.class})
public interface BookCreatureMapper {

    BookCreatureDto toDto(BookCreature entity);

    List<BookCreatureDto> toDtoList(List<BookCreature> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "coordinates", ignore = true)
    @Mapping(target = "creatureLocation", ignore = true)
    @Mapping(target = "ring", ignore = true)
    @Mapping(target = "name", qualifiedByName = "trim")
    void updateFields(BookCreatureDto dto, @MappingTarget BookCreature entity);
}
