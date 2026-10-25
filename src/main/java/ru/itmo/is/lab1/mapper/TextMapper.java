package ru.itmo.is.lab1.mapper;

import org.mapstruct.Named;

public class TextMapper {

    @Named("trim")
    public String trim(String value) {
        return value == null ? null : value.trim();
    }
}
