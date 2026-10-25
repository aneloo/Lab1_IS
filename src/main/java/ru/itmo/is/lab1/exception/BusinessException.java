package ru.itmo.is.lab1.exception;

import jakarta.ejb.ApplicationException;

import java.util.List;

@ApplicationException(rollback = true)
public class BusinessException extends RuntimeException {

    private final List<String> messages;

    public BusinessException(String message) {
        this(List.of(message));
    }

    public BusinessException(List<String> messages) {
        this(messages, null);
    }

    protected BusinessException(List<String> messages, Throwable cause) {
        super(String.join("; ", messages), cause);
        this.messages = List.copyOf(messages);
    }

    public List<String> getMessages() {
        return messages;
    }
}
