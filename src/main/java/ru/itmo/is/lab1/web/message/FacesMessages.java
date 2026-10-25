package ru.itmo.is.lab1.web.message;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public final class FacesMessages {

    private FacesMessages() {
    }

    public static void info(String summary) {
        add(FacesMessage.SEVERITY_INFO, summary);
    }

    public static void error(String summary) {
        add(FacesMessage.SEVERITY_ERROR, summary);
    }

    public static void keepForRedirect() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    private static void add(FacesMessage.Severity severity, String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, null));
    }
}
