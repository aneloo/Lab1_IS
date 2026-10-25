package ru.itmo.is.lab1.service;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.dto.CoordinatesDto;
import ru.itmo.is.lab1.dto.MagicCityDto;
import ru.itmo.is.lab1.dto.RingDto;
import ru.itmo.is.lab1.dto.RingUsageDto;
import ru.itmo.is.lab1.dto.UsageDto;
import ru.itmo.is.lab1.event.ChangeEvent;
import ru.itmo.is.lab1.exception.NotFoundException;
import ru.itmo.is.lab1.exception.TranslatePersistenceErrors;
import ru.itmo.is.lab1.mapper.CoordinatesMapper;
import ru.itmo.is.lab1.mapper.MagicCityMapper;
import ru.itmo.is.lab1.mapper.RingMapper;
import ru.itmo.is.lab1.model.BookCreature;
import ru.itmo.is.lab1.model.Coordinates;
import ru.itmo.is.lab1.model.MagicCity;
import ru.itmo.is.lab1.model.Ring;
import ru.itmo.is.lab1.repository.BookCreatureRepository;
import ru.itmo.is.lab1.repository.RelatedObjectsRepository;
import ru.itmo.is.lab1.repository.RingOwner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Stateless
@TranslatePersistenceErrors
public class RelatedObjectsService {

    @Inject
    private RelatedObjectsRepository related;

    @Inject
    private BookCreatureRepository creatures;

    @Inject
    private CreatureDeletionService deletion;

    @Inject
    private CoordinatesMapper coordinatesMapper;

    @Inject
    private MagicCityMapper cityMapper;

    @Inject
    private RingMapper ringMapper;

    @Inject
    private Event<ChangeEvent> changes;

    public List<CoordinatesDto> coordinatesOptions() {
        return coordinatesMapper.toDtoList(related.findAllCoordinates());
    }

    public List<MagicCityDto> cityOptions() {
        return cityMapper.toDtoList(related.findAllCities());
    }

    public List<RingDto> ringOptions(Long creatureId) {
        List<Ring> rings = new ArrayList<>(related.findFreeRings());
        if (creatureId != null) {
            creatures.findById(creatureId).map(BookCreature::getRing).ifPresent(rings::add);
        }
        rings.sort(Comparator.comparing(Ring::getId));
        return ringMapper.toDtoList(rings);
    }

    public List<UsageDto<CoordinatesDto>> coordinatesWithUsage() {
        Map<Long, Long> counts = creatures.countGroupedByCoordinates();
        return related.findAllCoordinates().stream()
                .map(c -> new UsageDto<>(coordinatesMapper.toDto(c), counts.getOrDefault(c.getId(), 0L)))
                .toList();
    }

    public List<UsageDto<MagicCityDto>> citiesWithUsage() {
        Map<Long, Long> counts = creatures.countGroupedByCity();
        return related.findAllCities().stream()
                .map(c -> new UsageDto<>(cityMapper.toDto(c), counts.getOrDefault(c.getId(), 0L)))
                .toList();
    }

    public List<RingUsageDto> ringsWithOwners() {
        Map<Long, RingOwner> owners = creatures.findRingOwners();
        return related.findAllRings().stream()
                .map(r -> {
                    Optional<RingOwner> owner = Optional.ofNullable(owners.get(r.getId()));
                    return new RingUsageDto(ringMapper.toDto(r),
                            owner.map(RingOwner::creatureId).orElse(null),
                            owner.map(RingOwner::creatureName).orElse(null));
                })
                .toList();
    }

    public int deleteCoordinates(long id, String actor) {
        Coordinates target = related.findCoordinatesForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Координаты #" + id + " не найдены"));
        List<BookCreature> linked = creatures.findByCoordinatesForUpdate(target);
        deletion.removeWithOrphans(linked);
        removeIfStillPresent(target);
        changes.fire(ChangeEvent.relatedDeleted(ChangeEvent.Type.COORDINATES_DELETED, id,
                linked.size(), actor));
        return linked.size();
    }

    public int deleteCity(long id, String actor) {
        MagicCity target = related.findCityForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Город #" + id + " не найден"));
        List<BookCreature> linked = creatures.findByCityForUpdate(target);
        deletion.removeWithOrphans(linked);
        removeIfStillPresent(target);
        changes.fire(ChangeEvent.relatedDeleted(ChangeEvent.Type.CITY_DELETED, id,
                linked.size(), actor));
        return linked.size();
    }

    public void deleteRing(long id, String actor) {
        Ring ring = related.findRingForUpdate(id)
                .orElseThrow(() -> new NotFoundException("Кольцо #" + id + " не найдено"));
        Optional<BookCreature> owner = creatures.findByRingForUpdate(ring);
        owner.ifPresent(o -> {
            creatures.forceVersionIncrement(o);
            o.setRing(null);
        });
        creatures.flush();
        related.remove(ring);
        related.flush();
        changes.fire(ChangeEvent.relatedDeleted(ChangeEvent.Type.RING_DELETED, id,
                owner.isPresent() ? 1 : 0, actor));
    }

    private void removeIfStillPresent(Object target) {
        if (related.isManaged(target)) {
            related.remove(target);
        }
        related.flush();
    }

}
