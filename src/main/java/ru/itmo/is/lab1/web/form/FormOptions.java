package ru.itmo.is.lab1.web.form;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import ru.itmo.is.lab1.domain.BookCreatureType;
import ru.itmo.is.lab1.dto.LinkMode;

import java.util.List;

@Named("options")
@ApplicationScoped
public class FormOptions {

    public BookCreatureType[] getCreatureTypes() {
        return BookCreatureType.values();
    }

    public List<LinkMode> getCreateModes() {
        return List.of(LinkMode.NEW, LinkMode.EXISTING);
    }

    public List<LinkMode> getEditModes() {
        return List.of(LinkMode.CURRENT, LinkMode.EXISTING, LinkMode.NEW);
    }

    public List<LinkMode> getRingCreateModes() {
        return List.of(LinkMode.NONE, LinkMode.NEW, LinkMode.EXISTING);
    }

    public List<LinkMode> getRingEditModes() {
        return List.of(LinkMode.NONE, LinkMode.CURRENT, LinkMode.EXISTING, LinkMode.NEW);
    }
}
