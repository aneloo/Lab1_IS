package ru.itmo.is.lab1.web.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.security.UserSession;
import ru.itmo.is.lab1.service.SpecialOperationsService;
import ru.itmo.is.lab1.web.message.FacesMessages;

import java.io.Serializable;
import java.util.OptionalDouble;

@Named("specialController")
@ViewScoped
public class SpecialOperationsController implements Serializable {

    @Inject
    private SpecialOperationsService service;

    @Inject
    private UserSession userSession;

    private boolean averageCalculated;
    private Double average;

    private Float lessThan;
    private Long lessCount;

    private Float greaterThan;
    private Long greaterCount;

    private Long firstId;
    private Long secondId;
    private boolean swapped;

    private BookCreatureDto strongest;
    private boolean strongestSearched;

    public void calculateAverage() {
        OptionalDouble result = service.averageAttackLevel();
        average = result.isPresent() ? result.getAsDouble() : null;
        averageCalculated = true;
    }

    public void countLess() {
        lessCount = service.countDefenseLessThan(lessThan);
    }

    public void countGreater() {
        greaterCount = service.countDefenseGreaterThan(greaterThan);
    }

    public void swap() {
        service.swapRings(firstId, secondId, userSession.getUsername());
        swapped = true;
        FacesMessages.info("Персонажи #" + firstId + " и #" + secondId + " обменялись кольцами");
    }

    public void findStrongest() {
        strongest = service.findWithStrongestRing().orElse(null);
        strongestSearched = true;
    }

    public boolean isAverageCalculated() { return averageCalculated; }
    public Double getAverage() { return average; }
    public Float getLessThan() { return lessThan; }
    public void setLessThan(Float lessThan) { this.lessThan = lessThan; }
    public Long getLessCount() { return lessCount; }
    public Float getGreaterThan() { return greaterThan; }
    public void setGreaterThan(Float greaterThan) { this.greaterThan = greaterThan; }
    public Long getGreaterCount() { return greaterCount; }
    public Long getFirstId() { return firstId; }
    public void setFirstId(Long firstId) { this.firstId = firstId; }
    public Long getSecondId() { return secondId; }
    public void setSecondId(Long secondId) { this.secondId = secondId; }
    public boolean isSwapped() { return swapped; }
    public BookCreatureDto getStrongest() { return strongest; }
    public boolean isStrongestSearched() { return strongestSearched; }
}
