package ru.itmo.is.lab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.itmo.is.lab1.dto.CoordinatesDto;
import ru.itmo.is.lab1.dto.LinkMode;
import ru.itmo.is.lab1.dto.MagicCityDto;
import ru.itmo.is.lab1.dto.RelationLink;
import ru.itmo.is.lab1.dto.RingDto;
import ru.itmo.is.lab1.exception.ConflictException;
import ru.itmo.is.lab1.exception.NotFoundException;
import ru.itmo.is.lab1.exception.ValidationException;
import ru.itmo.is.lab1.mapper.CoordinatesMapper;
import ru.itmo.is.lab1.mapper.MagicCityMapper;
import ru.itmo.is.lab1.mapper.RingMapper;
import ru.itmo.is.lab1.model.BookCreature;
import ru.itmo.is.lab1.model.Coordinates;
import ru.itmo.is.lab1.model.MagicCity;
import ru.itmo.is.lab1.model.Ring;
import ru.itmo.is.lab1.repository.BookCreatureRepository;
import ru.itmo.is.lab1.repository.RelatedObjectsRepository;

import java.util.Objects;
import java.util.function.Supplier;

@ApplicationScoped
public class RelatedObjectResolver {

    @Inject
    private RelatedObjectsRepository related;

    @Inject
    private BookCreatureRepository creatures;

    @Inject
    private CoordinatesMapper coordinatesMapper;

    @Inject
    private MagicCityMapper cityMapper;

    @Inject
    private RingMapper ringMapper;

    public Coordinates resolveCoordinates(RelationLink<CoordinatesDto> link, Coordinates current) {
        requireMode(link, "координат");
        return switch (link.mode()) {
            case NEW -> coordinatesMapper.toEntity(dataOrEmpty(link.data(), CoordinatesDto::new));
            case EXISTING -> related.findCoordinates(requireId(link, "координаты"))
                    .orElseThrow(() -> new NotFoundException("Координаты #" + link.existingId() + " не найдены"));
            case CURRENT -> {
                requireCurrent(current, "координат");
                if (link.data() != null) {
                    requireSameVersion(link.data().getVersion(), current.getVersion(), "Текущие координаты");
                    coordinatesMapper.update(link.data(), current);
                }
                yield current;
            }
            case NONE -> throw new ValidationException("Координаты обязательны");
        };
    }

    public MagicCity resolveCity(RelationLink<MagicCityDto> link, MagicCity current) {
        requireMode(link, "города");
        return switch (link.mode()) {
            case NEW -> cityMapper.toEntity(dataOrEmpty(link.data(), MagicCityDto::new));
            case EXISTING -> related.findCity(requireId(link, "город"))
                    .orElseThrow(() -> new NotFoundException("Город #" + link.existingId() + " не найден"));
            case CURRENT -> {
                requireCurrent(current, "города");
                if (link.data() != null) {
                    requireSameVersion(link.data().getVersion(), current.getVersion(), "Текущий город");
                    cityMapper.update(link.data(), current);
                }
                yield current;
            }
            case NONE -> throw new ValidationException("Город обязателен");
        };
    }

    public Ring resolveRing(RelationLink<RingDto> link, Ring current, Long ownerId) {
        if (link == null || link.mode() == null || link.is(LinkMode.NONE)) {
            return null;
        }
        return switch (link.mode()) {
            case NEW -> ringMapper.toEntity(dataOrEmpty(link.data(), RingDto::new));
            case EXISTING -> lockFreeRing(requireId(link, "кольцо"), ownerId);
            case CURRENT -> {
                requireCurrent(current, "кольца");
                if (link.data() != null) {
                    requireSameVersion(link.data().getVersion(), current.getVersion(), "Текущее кольцо");
                    ringMapper.update(link.data(), current);
                }
                yield current;
            }
            case NONE -> null;
        };
    }

    public void persistNew(BookCreature creature) {
        if (creature.getCoordinates().getId() == null) {
            related.persist(creature.getCoordinates());
        }
        if (creature.getCreatureLocation().getId() == null) {
            related.persist(creature.getCreatureLocation());
        }
        if (creature.getRing() != null && creature.getRing().getId() == null) {
            related.persist(creature.getRing());
        }
    }

    private Ring lockFreeRing(long ringId, Long ownerId) {
        Ring ring = related.findRingForUpdate(ringId)
                .orElseThrow(() -> new NotFoundException("Кольцо #" + ringId + " не найдено"));
        creatures.findByRing(ring)
                .filter(owner -> !Objects.equals(owner.getId(), ownerId))
                .ifPresent(owner -> {
                    throw new ConflictException("Кольцо #" + ringId + " уже принадлежит персонажу #"
                            + owner.getId() + " «" + owner.getName() + "». Для передачи колец используйте обмен.");
                });
        return ring;
    }

    private static <T> T dataOrEmpty(T data, Supplier<T> empty) {
        return data == null ? empty.get() : data;
    }

    private static void requireMode(RelationLink<?> link, String what) {
        if (link == null || link.mode() == null) {
            throw new ValidationException("Не выбран способ задания " + what);
        }
    }

    private static long requireId(RelationLink<?> link, String what) {
        if (link.existingId() == null) {
            throw new ValidationException("Выберите " + what + " из списка");
        }
        return link.existingId();
    }

    private static void requireCurrent(Object current, String what) {
        if (current == null) {
            throw new ValidationException("У объекта нет текущего " + what + " для изменения");
        }
    }

    private static void requireSameVersion(Long expected, long actual, String what) {
        if (expected == null || expected != actual) {
            throw new ConflictException(what + " уже изменил другой пользователь. Откройте редактирование заново.");
        }
    }
}
