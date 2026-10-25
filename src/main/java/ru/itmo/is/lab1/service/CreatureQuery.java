package ru.itmo.is.lab1.service;

import java.util.Map;

public record CreatureQuery(int first, int pageSize, String sortField, boolean ascending,
                            Map<String, String> filters) {
}
