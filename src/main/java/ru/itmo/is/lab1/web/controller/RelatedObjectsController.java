package ru.itmo.is.lab1.web.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.itmo.is.lab1.dto.CoordinatesDto;
import ru.itmo.is.lab1.dto.MagicCityDto;
import ru.itmo.is.lab1.dto.RingUsageDto;
import ru.itmo.is.lab1.dto.UsageDto;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.RelatedObjectsService;
import ru.itmo.is.lab1.web.message.FacesMessages;

import java.io.Serializable;
import java.util.List;

@Named("relatedController")
@ViewScoped
public class RelatedObjectsController implements Serializable {

    @Inject
    private RelatedObjectsService service;

    @Inject
    private UserSession userSession;

    private List<UsageDto<CoordinatesDto>> coordinates;
    private List<UsageDto<MagicCityDto>> cities;
    private List<RingUsageDto> rings;

    @PostConstruct
    public void load() {
        coordinates = service.coordinatesWithUsage();
        cities = service.citiesWithUsage();
        rings = service.ringsWithOwners();
    }

    public void deleteCoordinates(long id) {
        int deleted = service.deleteCoordinates(id, userSession.getUsername());
        FacesMessages.info("Координаты #" + id + " удалены; удалено связанных персонажей: " + deleted);
        load();
    }

    public void deleteCity(long id) {
        int deleted = service.deleteCity(id, userSession.getUsername());
        FacesMessages.info("Город #" + id + " удалён; удалено связанных персонажей: " + deleted);
        load();
    }

    public void deleteRing(long id) {
        service.deleteRing(id, userSession.getUsername());
        FacesMessages.info("Кольцо #" + id + " удалено");
        load();
    }

    public List<UsageDto<CoordinatesDto>> getCoordinates() { return coordinates; }
    public List<UsageDto<MagicCityDto>> getCities() { return cities; }
    public List<RingUsageDto> getRings() { return rings; }
}
