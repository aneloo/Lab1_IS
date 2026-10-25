package ru.itmo.is.lab1.dto;

public record CreatureRequest(BookCreatureDto creature,
                              RelationLink<CoordinatesDto> coordinates,
                              RelationLink<MagicCityDto> city,
                              RelationLink<RingDto> ring) {
}
