package ru.itmo.is.lab1.repository;

import java.util.Map;

public record CreatureCriteria(int first, int maxResults, CreatureColumn sortColumn, boolean ascending,
                               Map<CreatureColumn, Object> equalTo) {
}
