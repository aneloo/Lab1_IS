package ru.itmo.is.lab1.exception;

import java.util.List;

public class DataIntegrityException extends BusinessException {

    public DataIntegrityException(String message, Throwable cause) {
        super(List.of(message), cause);
    }
}
