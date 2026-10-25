package ru.itmo.is.lab1.web.model;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.service.BookCreatureService;
import ru.itmo.is.lab1.service.CreatureQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreatureLazyModel extends LazyDataModel<BookCreatureDto> {

    private final BookCreatureService service;

    public CreatureLazyModel(BookCreatureService service) {
        this.service = service;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        return Math.toIntExact(service.count(toQuery(0, 0, Map.of(), filterBy)));
    }

    @Override
    public List<BookCreatureDto> load(int first, int pageSize, Map<String, SortMeta> sortBy,
                                      Map<String, FilterMeta> filterBy) {
        return service.findPage(toQuery(first, pageSize, sortBy, filterBy));
    }

    @Override
    public String getRowKey(BookCreatureDto creature) {
        return String.valueOf(creature.getId());
    }

    @Override
    public BookCreatureDto getRowData(String rowKey) {
        try {
            return service.findById(Long.parseLong(rowKey)).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static CreatureQuery toQuery(int first, int pageSize, Map<String, SortMeta> sortBy,
                                         Map<String, FilterMeta> filterBy) {
        String sortField = null;
        boolean ascending = true;
        if (sortBy != null) {
            for (SortMeta meta : sortBy.values()) {
                if (meta.getOrder() != null && meta.getOrder() != SortOrder.UNSORTED) {
                    sortField = meta.getField();
                    ascending = meta.getOrder() == SortOrder.ASCENDING;
                    break;
                }
            }
        }
        Map<String, String> filters = new HashMap<>();
        if (filterBy != null) {
            for (FilterMeta meta : filterBy.values()) {
                Object value = meta.getFilterValue();
                if (value != null && !value.toString().isEmpty()) {
                    filters.put(meta.getField(), value.toString());
                }
            }
        }
        return new CreatureQuery(first, pageSize, sortField, ascending, filters);
    }
}
