package ru.itmo.is.lab1.web.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.BookCreatureService;
import ru.itmo.is.lab1.web.form.CreatureForm;
import ru.itmo.is.lab1.web.form.CreatureFormMapper;
import ru.itmo.is.lab1.web.form.CreatureFormOptionsLoader;
import ru.itmo.is.lab1.web.message.FacesMessages;

import java.io.Serializable;

@Named("createController")
@ViewScoped
public class CreateCreatureController implements Serializable {

    @Inject
    private BookCreatureService service;

    @Inject
    private CreatureFormMapper formMapper;

    @Inject
    private CreatureFormOptionsLoader optionsLoader;

    @Inject
    private UserSession userSession;

    private CreatureForm form;

    @PostConstruct
    public void init() {
        form = formMapper.toCreateForm();
        refreshOptions();
    }

    public void refreshOptions() {
        optionsLoader.load(form, null);
    }

    public String save() {
        BookCreatureDto created = service.create(formMapper.toRequest(form), userSession.getUsername());
        FacesMessages.info("Объект #" + created.getId() + " «" + created.getName() + "» создан");
        FacesMessages.keepForRedirect();
        return "view?faces-redirect=true&id=" + created.getId();
    }

    public CreatureForm getForm() {
        return form;
    }
}
