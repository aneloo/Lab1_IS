package ru.itmo.is.lab1.dto;

import java.io.Serializable;

public record UsageDto<T extends Serializable>(T item, long creatures) implements Serializable {
}
