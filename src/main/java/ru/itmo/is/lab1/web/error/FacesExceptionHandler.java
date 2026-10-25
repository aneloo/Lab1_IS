package ru.itmo.is.lab1.web.error;

import jakarta.faces.application.ViewExpiredException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.PhaseId;
import ru.itmo.is.lab1.exception.BusinessException;
import ru.itmo.is.lab1.web.message.FacesMessages;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacesExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger LOG = Logger.getLogger(FacesExceptionHandler.class.getName());

    public FacesExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void handle() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null && context.getCurrentPhaseId() != PhaseId.RENDER_RESPONSE) {
            Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
            while (events.hasNext()) {
                Throwable error = events.next().getContext().getException();
                if (find(error, ViewExpiredException.class).isPresent()) {
                    continue;
                }
                report(error);
                context.validationFailed();
                events.remove();
            }
        }
        getWrapped().handle();
    }

    private static void report(Throwable error) {
        Optional<BusinessException> business = find(error, BusinessException.class);
        if (business.isPresent()) {
            business.get().getMessages().forEach(FacesMessages::error);
            return;
        }
        String incident = UUID.randomUUID().toString().substring(0, 8);
        LOG.log(Level.SEVERE, "Необработанная ошибка, код " + incident, error);
        FacesMessages.error("Операция не выполнена из-за внутренней ошибки сервера (код " + incident + ")");
    }

    private static <T extends Throwable> Optional<T> find(Throwable error, Class<T> type) {
        for (Throwable t = error; t != null; t = t.getCause()) {
            if (type.isInstance(t)) {
                return Optional.of(type.cast(t));
            }
        }
        return Optional.empty();
    }
}
