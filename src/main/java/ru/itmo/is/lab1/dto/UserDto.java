package ru.itmo.is.lab1.dto;

import java.io.Serializable;

public record UserDto(Long id, String username) implements Serializable {
}
