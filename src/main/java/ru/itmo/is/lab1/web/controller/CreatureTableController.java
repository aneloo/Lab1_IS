package ru.itmo.is.lab1.web.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.BookCreatureService;
import ru.itmo.is.lab1.service.CreatureDeletionService;
import ru.itmo.is.lab1.web.message.FacesMessages;
import ru.itmo.is.lab1.web.model.CreatureLazyModel;

import java.io.Serializable;

@Named("tableController")
@ViewScoped
public class CreatureTableController implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    private CreatureDeletionService deletion;

    @Inject
    private UserSession userSession;

    private CreatureLazyModel model;

    @PostConstruct
    public void init() {
        model = new CreatureLazyModel(service);
    }

    public void delete(long id) {
        deletion.delete(id, userSession.getUsername());
        FacesMessages.info("Объект #" + id + " удалён вместе со связанными объектами");
    }

    public CreatureLazyModel getModel() {
        return model;
    }
}
