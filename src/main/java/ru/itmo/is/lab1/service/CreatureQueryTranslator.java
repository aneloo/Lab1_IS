package ru.itmo.is.lab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import ru.itmo.is.lab1.domain.BookCreatureType;
import ru.itmo.is.lab1.repository.CreatureColumn;
import ru.itmo.is.lab1.repository.CreatureCriteria;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class CreatureQueryTranslator {

    private static final Map<String, CreatureColumn> SORTABLE = Map.ofEntries(
            Map.entry("id", CreatureColumn.ID),
            Map.entry("name", CreatureColumn.NAME),
            Map.entry("x", CreatureColumn.X),
            Map.entry("y", CreatureColumn.Y),
            Map.entry("creationDate", CreatureColumn.CREATION_DATE),
            Map.entry("age", CreatureColumn.AGE),
            Map.entry("creatureType", CreatureColumn.CREATURE_TYPE),
            Map.entry("cityName", CreatureColumn.CITY_NAME),
            Map.entry("attackLevel", CreatureColumn.ATTACK_LEVEL),
            Map.entry("defenseLevel", CreatureColumn.DEFENSE_LEVEL),
            Map.entry("ringName", CreatureColumn.RING_NAME),
            Map.entry("ringWeight", CreatureColumn.RING_WEIGHT)
    );

    private static final Map<String, CreatureColumn> FILTERABLE = Map.of(
            "name", CreatureColumn.NAME,
            "creatureType", CreatureColumn.CREATURE_TYPE,
            "cityName", CreatureColumn.CITY_NAME,
            "ringName", CreatureColumn.RING_NAME
    );

    public Optional<CreatureCriteria> translate(CreatureQuery query) {
        Map<CreatureColumn, Object> equalTo = new EnumMap<>(CreatureColumn.class);
        if (query.filters() != null) {
            for (Map.Entry<String, String> filter : query.filters().entrySet()) {
                CreatureColumn column = FILTERABLE.get(filter.getKey());
                String value = filter.getValue();
                if (column == null || value == null || value.isEmpty()) {
                    continue;
                }
                if (column == CreatureColumn.CREATURE_TYPE) {
                    Optional<BookCreatureType> type = parseType(value);
                    if (type.isEmpty()) {
                        return Optional.empty();
                    }
                    equalTo.put(column, type.get());
                } else {
                    equalTo.put(column, value);
                }
            }
        }
        CreatureColumn sort = query.sortField() == null ? null : SORTABLE.get(query.sortField());
        return Optional.of(new CreatureCriteria(query.first(), query.pageSize(), sort, query.ascending(), equalTo));
    }

    private static Optional<BookCreatureType> parseType(String value) {
        for (BookCreatureType type : BookCreatureType.values()) {
            if (type.name().equals(value)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
