package ru.itmo.is.lab1.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.mapstruct.factory.Mappers;

@ApplicationScoped
public class MapperProducer {

    @Produces
    @ApplicationScoped
    public BookCreatureMapper bookCreatureMapper() {
        return Mappers.getMapper(BookCreatureMapper.class);
    }

    @Produces
    @ApplicationScoped
    public CoordinatesMapper coordinatesMapper() {
        return Mappers.getMapper(CoordinatesMapper.class);
    }

    @Produces
    @ApplicationScoped
    public MagicCityMapper magicCityMapper() {
        return Mappers.getMapper(MagicCityMapper.class);
    }

    @Produces
    @ApplicationScoped
    public RingMapper ringMapper() {
        return Mappers.getMapper(RingMapper.class);
    }

    @Produces
    @ApplicationScoped
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}
