package ru.itmo.is.lab1.exception;

import java.util.List;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String> messages) {
        super(messages);
    }
}
