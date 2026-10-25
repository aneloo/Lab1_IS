package ru.itmo.is.lab1.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.dto.CreatureRequest;
import ru.itmo.is.lab1.event.ChangeEvent;
import ru.itmo.is.lab1.exception.ConflictException;
import ru.itmo.is.lab1.exception.NotFoundException;
import ru.itmo.is.lab1.exception.TranslatePersistenceErrors;
import ru.itmo.is.lab1.mapper.BookCreatureMapper;
import ru.itmo.is.lab1.model.BookCreature;
import ru.itmo.is.lab1.model.Coordinates;
import ru.itmo.is.lab1.model.MagicCity;
import ru.itmo.is.lab1.model.Ring;
import ru.itmo.is.lab1.repository.BookCreatureRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Stateless
@TranslatePersistenceErrors
public class BookCreatureService {

    @Inject
    private BookCreatureRepository creatures;

    @Inject
    private CreatureQueryTranslator queryTranslator;

    @Inject
    private RelatedObjectResolver resolver;

    @Inject
    private CreatureValidator validator;

    @Inject
    private BookCreatureMapper mapper;

    @Inject
    private Event<ChangeEvent> changes;

    public List<BookCreatureDto> findPage(CreatureQuery query) {
        return queryTranslator.translate(query)
                .map(criteria -> mapper.toDtoList(creatures.findPage(criteria)))
                .orElse(List.of());
    }

    public long count(CreatureQuery query) {
        return queryTranslator.translate(query).map(creatures::count).orElse(0L);
    }

    public Optional<BookCreatureDto> findById(long id) {
        return creatures.findById(id).map(mapper::toDto);
    }

    public BookCreatureDto getById(long id) {
        return findById(id).orElseThrow(() -> notFound(id));
    }

    public BookCreatureDto create(CreatureRequest request, String actor) {
        validator.requireComplete(request);
        BookCreature creature = new BookCreature();
        mapper.updateFields(request.creature(), creature);
        creature.setCreationDate(LocalDateTime.now());
        creature.setRing(resolver.resolveRing(request.ring(), null, null));
        creature.setCoordinates(resolver.resolveCoordinates(request.coordinates(), null));
        creature.setCreatureLocation(resolver.resolveCity(request.city(), null));

        validator.validate(creature);
        resolver.persistNew(creature);
        creatures.persist(creature);
        creatures.flush();

        changes.fire(ChangeEvent.creature(ChangeEvent.Type.CREATURE_CREATED, creature.getId(),
                creature.getName(), actor));
        return mapper.toDto(creature);
    }

    public BookCreatureDto update(long id, CreatureRequest request, String actor) {
        validator.requireComplete(request);
        BookCreature creature = creatures.findByIdForUpdate(id).orElseThrow(() -> notFound(id));
        Long expectedVersion = request.creature().getVersion();
        if (expectedVersion == null || expectedVersion != creature.getVersion()) {
            throw new ConflictException("Объект #" + id + " уже изменил другой пользователь. "
                    + "Закройте окно и откройте редактирование заново, чтобы увидеть актуальные данные.");
        }

        Ring ring = resolver.resolveRing(request.ring(), creature.getRing(), creature.getId());
        Coordinates coordinates = resolver.resolveCoordinates(request.coordinates(), creature.getCoordinates());
        MagicCity city = resolver.resolveCity(request.city(), creature.getCreatureLocation());
        mapper.updateFields(request.creature(), creature);
        creature.setRing(ring);
        creature.setCoordinates(coordinates);
        creature.setCreatureLocation(city);

        validator.validate(creature);
        resolver.persistNew(creature);
        creatures.forceVersionIncrement(creature);
        creatures.flush();

        changes.fire(ChangeEvent.creature(ChangeEvent.Type.CREATURE_UPDATED, creature.getId(),
                creature.getName(), actor));
        return mapper.toDto(creature);
    }

    private static NotFoundException notFound(long id) {
        return new NotFoundException("Объект #" + id + " не найден: возможно, его уже удалили");
    }
}
