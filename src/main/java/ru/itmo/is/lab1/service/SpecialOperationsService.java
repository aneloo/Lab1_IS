package ru.itmo.is.lab1.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.event.ChangeEvent;
import ru.itmo.is.lab1.exception.NotFoundException;
import ru.itmo.is.lab1.exception.TranslatePersistenceErrors;
import ru.itmo.is.lab1.exception.ValidationException;
import ru.itmo.is.lab1.mapper.BookCreatureMapper;
import ru.itmo.is.lab1.model.BookCreature;
import ru.itmo.is.lab1.model.Ring;
import ru.itmo.is.lab1.repository.BookCreatureRepository;

import java.util.Optional;
import java.util.OptionalDouble;

@Stateless
@TranslatePersistenceErrors
public class SpecialOperationsService {

    @Inject
    private BookCreatureRepository creatures;

    @Inject
    private BookCreatureMapper mapper;

    @Inject
    private Event<ChangeEvent> changes;

    public OptionalDouble averageAttackLevel() {
        return creatures.findAllAttackLevels().stream().mapToInt(Integer::intValue).average();
    }

    public long countDefenseLessThan(Float value) {
        return creatures.countDefenseLessThan(requireValue(value));
    }

    public long countDefenseGreaterThan(Float value) {
        return creatures.countDefenseGreaterThan(requireValue(value));
    }

    public void swapRings(Long firstId, Long secondId, String actor) {
        if (firstId == null || secondId == null) {
            throw new ValidationException("Для обмена нужно указать двух персонажей");
        }
        if (firstId.equals(secondId)) {
            throw new ValidationException("Для обмена нужно указать двух разных персонажей");
        }

        long lowId = Math.min(firstId, secondId);
        long highId = Math.max(firstId, secondId);
        BookCreature low = creatures.findByIdForUpdate(lowId)
                .orElseThrow(() -> new NotFoundException("Персонаж #" + lowId + " не найден"));
        BookCreature high = creatures.findByIdForUpdate(highId)
                .orElseThrow(() -> new NotFoundException("Персонаж #" + highId + " не найден"));

        Ring lowRing = low.getRing();
        Ring highRing = high.getRing();
        if (lowRing == null && highRing == null) {
            throw new ValidationException("У обоих персонажей нет колец - обменивать нечего");
        }

        creatures.forceVersionIncrement(low);
        creatures.forceVersionIncrement(high);
        low.setRing(null);
        high.setRing(null);
        creatures.flush();
        low.setRing(highRing);
        high.setRing(lowRing);
        creatures.flush();

        changes.fire(ChangeEvent.ringsSwapped(firstId, secondId, actor));
    }

    public Optional<BookCreatureDto> findWithStrongestRing() {
        return creatures.findWithStrongestRing().map(mapper::toDto);
    }

    private static float requireValue(Float value) {
        if (value == null) {
            throw new ValidationException("Введите уровень защиты");
        }
        return value;
    }
}
