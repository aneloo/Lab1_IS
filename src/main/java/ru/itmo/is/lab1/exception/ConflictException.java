package ru.itmo.is.lab1.exception;

import java.util.List;

public class ConflictException extends BusinessException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(List.of(message), cause);
    }
}
