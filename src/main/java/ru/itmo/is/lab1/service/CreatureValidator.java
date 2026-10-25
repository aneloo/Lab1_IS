package ru.itmo.is.lab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import ru.itmo.is.lab1.dto.CreatureRequest;
import ru.itmo.is.lab1.exception.ValidationException;
import ru.itmo.is.lab1.model.BookCreature;

import java.util.List;

@ApplicationScoped
public class CreatureValidator {

    @Inject
    private Validator validator;

    public void requireComplete(CreatureRequest request) {
        if (request == null || request.creature() == null) {
            throw new ValidationException("Не переданы данные объекта");
        }
    }

    public void validate(BookCreature creature) {
        List<String> messages = validator.validate(creature).stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .toList();
        if (!messages.isEmpty()) {
            throw new ValidationException(messages);
        }
    }
}
