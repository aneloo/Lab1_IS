package ru.itmo.is.lab1.web.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.BookCreatureService;
import ru.itmo.is.lab1.service.CreatureDeletionService;
import ru.itmo.is.lab1.web.message.FacesMessages;

import java.io.Serializable;
import java.util.Optional;

@Named("viewController")
@ViewScoped
public class ViewCreatureController implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    private CreatureDeletionService deletion;

    @Inject
    private UserSession userSession;

    private Long id;
    private BookCreatureDto creature;
    private boolean searched;

    public void onLoad() {
        if (id != null && !searched) {
            find();
        }
    }

    public void find() {
        searched = true;
        creature = id == null ? null : service.findById(id).orElse(null);
    }

    public void refresh() {
        if (creature == null) {
            return;
        }
        long current = creature.getId();
        Optional<BookCreatureDto> fresh = service.findById(current);
        creature = fresh.orElse(null);
        if (fresh.isEmpty()) {
            FacesMessages.error("Объект #" + current + " был удалён другим пользователем");
            PrimeFaces.current().ajax().update("viewForm:messages");
        }
    }

    public void delete() {
        if (creature == null) {
            return;
        }
        long deletedId = creature.getId();
        deletion.delete(deletedId, userSession.getUsername());
        FacesMessages.info("Объект #" + deletedId + " удалён вместе со связанными объектами");
        creature = null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BookCreatureDto getCreature() { return creature; }
    public boolean isSearched() { return searched; }
}
