package ru.itmo.is.lab1.exception;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PessimisticLockException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.sql.SQLException;
import java.util.List;

@TranslatePersistenceErrors
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class PersistenceErrorTranslator {

    private static final String INTEGRITY_VIOLATION_CLASS = "23";
    private static final String UNIQUE_VIOLATION = "23505";
    private static final String FOREIGN_KEY_VIOLATION = "23503";
    private static final String DEADLOCK_DETECTED = "40P01";
    private static final String SERIALIZATION_FAILURE = "40001";

    @AroundInvoke
    public Object translate(InvocationContext context) throws Exception {
        try {
            return context.proceed();
        } catch (BusinessException e) {
            throw e;
        } catch (RuntimeException e) {
            BusinessException translated = translate(e);
            if (translated == null) {
                throw e;
            }
            throw translated;
        }
    }

    private static BusinessException translate(RuntimeException e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof OptimisticLockException) {
                return new ConflictException("Объект одновременно изменил другой пользователь. "
                        + "Обновите данные и повторите действие.", e);
            }
            if (t instanceof PessimisticLockException || t instanceof LockTimeoutException) {
                return new ConflictException("Объект сейчас изменяет другой пользователь. Повторите действие позже.", e);
            }
            if (t instanceof ConstraintViolationException cve) {
                List<String> messages = cve.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .sorted()
                        .toList();
                return new ValidationException(messages);
            }
            if (t instanceof SQLException sql && isConcurrencyFailure(sql.getSQLState())) {
                return new ConflictException("Объект одновременно изменяли несколько пользователей. "
                        + "Обновите данные и повторите действие.", e);
            }
            if (t instanceof SQLException sql && isIntegrityViolation(sql.getSQLState())) {
                return new DataIntegrityException(describe(sql.getSQLState()), e);
            }
        }
        return null;
    }

    private static boolean isConcurrencyFailure(String sqlState) {
        return DEADLOCK_DETECTED.equals(sqlState) || SERIALIZATION_FAILURE.equals(sqlState);
    }

    private static boolean isIntegrityViolation(String sqlState) {
        return sqlState != null && sqlState.startsWith(INTEGRITY_VIOLATION_CLASS);
    }

    private static String describe(String sqlState) {
        if (UNIQUE_VIOLATION.equals(sqlState)) {
            return "Нарушена уникальность данных: возможно, этот объект уже занят другим персонажем.";
        }
        if (FOREIGN_KEY_VIOLATION.equals(sqlState)) {
            return "Связанный объект был удалён другим пользователем. Обновите данные и повторите действие.";
        }
        return "База данных отклонила изменение. Проверьте введённые данные.";
    }
}
