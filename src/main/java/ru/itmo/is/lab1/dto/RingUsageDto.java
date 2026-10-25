package ru.itmo.is.lab1.dto;

import java.io.Serializable;

public record RingUsageDto(RingDto ring, Long ownerId, String ownerName) implements Serializable {

    public boolean hasOwner() {
        return ownerId != null;
    }
}
