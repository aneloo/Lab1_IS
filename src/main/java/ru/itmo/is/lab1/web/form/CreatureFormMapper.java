package ru.itmo.is.lab1.web.form;

import jakarta.enterprise.context.ApplicationScoped;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.dto.CreatureRequest;
import ru.itmo.is.lab1.dto.LinkMode;
import ru.itmo.is.lab1.dto.RelationLink;
import ru.itmo.is.lab1.dto.RingDto;

@ApplicationScoped
public class CreatureFormMapper {

    public CreatureForm toCreateForm() {
        return new CreatureForm();
    }

    public CreatureForm toEditForm(BookCreatureDto creature) {
        CreatureForm form = new CreatureForm();
        form.setCreature(creature);

        form.setCoordinatesMode(LinkMode.CURRENT);
        form.setCoordinatesId(creature.getCoordinates().getId());
        form.setCoordinates(creature.getCoordinates());

        form.setCityMode(LinkMode.CURRENT);
        form.setCityId(creature.getCreatureLocation().getId());
        form.setCity(creature.getCreatureLocation());

        RingDto ring = creature.getRing();
        form.setRingMode(ring == null ? LinkMode.NONE : LinkMode.CURRENT);
        form.setRingId(ring == null ? null : ring.getId());
        form.setRing(ring == null ? new RingDto() : ring);
        return form;
    }

    public CreatureRequest toRequest(CreatureForm form) {
        return new CreatureRequest(form.getCreature(),
                new RelationLink<>(form.getCoordinatesMode(), form.getCoordinatesId(), form.getCoordinates()),
                new RelationLink<>(form.getCityMode(), form.getCityId(), form.getCity()),
                new RelationLink<>(form.getRingMode(), form.getRingId(), form.getRing()));
    }
}
