package ru.itmo.is.lab1.dto;

public record RelationLink<T>(LinkMode mode, Long existingId, T data) {

    public boolean is(LinkMode expected) {
        return mode == expected;
    }
}
