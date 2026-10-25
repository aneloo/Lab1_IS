package ru.itmo.is.lab1.web.form;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.service.RelatedObjectsService;

@ApplicationScoped
public class CreatureFormOptionsLoader {

    @Inject
    private RelatedObjectsService relatedObjects;

    public void load(CreatureForm form, Long creatureId) {
        form.setOptions(relatedObjects.coordinatesOptions(), relatedObjects.cityOptions(),
                relatedObjects.ringOptions(creatureId));
    }
}
