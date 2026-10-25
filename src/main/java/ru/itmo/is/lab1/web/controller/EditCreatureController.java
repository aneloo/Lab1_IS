package ru.itmo.is.lab1.web.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.BookCreatureService;
import ru.itmo.is.lab1.web.form.CreatureForm;
import ru.itmo.is.lab1.web.form.CreatureFormMapper;
import ru.itmo.is.lab1.web.form.CreatureFormOptionsLoader;
import ru.itmo.is.lab1.web.message.FacesMessages;

import java.io.Serializable;

@Named("editController")
@ViewScoped
public class EditCreatureController implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    private CreatureFormMapper formMapper;

    @Inject
    private CreatureFormOptionsLoader optionsLoader;

    @Inject
    private UserSession userSession;

    private Long id;
    private CreatureForm form = new CreatureForm();

    public void open(long id) {
        BookCreatureDto creature = service.getById(id);
        this.id = creature.getId();
        form = formMapper.toEditForm(creature);
        refreshOptions();
    }

    public void refreshOptions() {
        optionsLoader.load(form, id);
    }

    public void save(String afterSave) {
        BookCreatureDto updated = service.update(id, formMapper.toRequest(form), userSession.getUsername());
        FacesMessages.info("Объект #" + updated.getId() + " сохранён");
        PrimeFaces.current().ajax().addCallbackParam("saved", true);
        if (afterSave != null && !afterSave.isBlank()) {
            PrimeFaces.current().ajax().update(afterSave.split("\\s+"));
        }
    }

    public CreatureForm getForm() {
        return form;
    }

    public boolean isLoaded() {
        return id != null;
    }

    public Long getId() {
        return id;
    }
}
