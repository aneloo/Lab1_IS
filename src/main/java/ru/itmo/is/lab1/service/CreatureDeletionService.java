package ru.itmo.is.lab1.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.event.ChangeEvent;
import ru.itmo.is.lab1.exception.NotFoundException;
import ru.itmo.is.lab1.exception.TranslatePersistenceErrors;
import ru.itmo.is.lab1.model.BookCreature;
import ru.itmo.is.lab1.model.Coordinates;
import ru.itmo.is.lab1.model.MagicCity;
import ru.itmo.is.lab1.model.Ring;
import ru.itmo.is.lab1.repository.BookCreatureRepository;
import ru.itmo.is.lab1.repository.RelatedObjectsRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Stateless
@TranslatePersistenceErrors
public class CreatureDeletionService {

    @Inject
    private BookCreatureRepository creatures;

    @Inject
    private RelatedObjectsRepository related;

    @Inject
    private Event<ChangeEvent> changes;

    public void delete(long id, String actor) {
        BookCreature creature = creatures.findByIdForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Объект #" + id + " не найден: возможно, его уже удалили"));
        String name = creature.getName();
        removeWithOrphans(List.of(creature));
        changes.fire(ChangeEvent.creature(ChangeEvent.Type.CREATURE_DELETED, id, name, actor));
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void removeWithOrphans(List<BookCreature> victims) {
        if (victims.isEmpty()) {
            return;
        }
        List<Coordinates> coordinates = victims.stream().map(BookCreature::getCoordinates)
                .filter(Objects::nonNull).distinct()
                .sorted(Comparator.comparing(Coordinates::getId)).toList();
        List<MagicCity> cities = victims.stream().map(BookCreature::getCreatureLocation)
                .filter(Objects::nonNull).distinct()
                .sorted(Comparator.comparing(MagicCity::getId)).toList();
        List<Ring> rings = victims.stream().map(BookCreature::getRing)
                .filter(Objects::nonNull).toList();

        related.lockCoordinates(coordinates);
        related.lockCities(cities);

        victims.forEach(creatures::remove);
        creatures.flush();

        rings.forEach(related::remove);
        Map<Long, Long> coordinatesUsage = creatures.countByCoordinates(coordinates);
        coordinates.stream()
                .filter(c -> !coordinatesUsage.containsKey(c.getId()))
                .forEach(related::remove);
        Map<Long, Long> citiesUsage = creatures.countByCities(cities);
        cities.stream()
                .filter(c -> !citiesUsage.containsKey(c.getId()))
                .forEach(related::remove);
        creatures.flush();
    }
}
