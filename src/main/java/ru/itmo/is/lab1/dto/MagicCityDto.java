package ru.itmo.is.lab1.dto;

import ru.itmo.is.lab1.domain.BookCreatureType;

import java.io.Serializable;
import java.time.LocalDate;

public class MagicCityDto implements Serializable {

    private Long id;
    private Long version;
    private String name;
    private Double area;
    private Long population;
    private LocalDate establishmentDate;
    private BookCreatureType governor;
    private Boolean capital;
    private Integer populationDensity;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getArea() { return area; }
    public void setArea(Double area) { this.area = area; }
    public Long getPopulation() { return population; }
    public void setPopulation(Long population) { this.population = population; }
    public LocalDate getEstablishmentDate() { return establishmentDate; }
    public void setEstablishmentDate(LocalDate establishmentDate) { this.establishmentDate = establishmentDate; }
    public BookCreatureType getGovernor() { return governor; }
    public void setGovernor(BookCreatureType governor) { this.governor = governor; }
    public Boolean getCapital() { return capital; }
    public void setCapital(Boolean capital) { this.capital = capital; }
    public Integer getPopulationDensity() { return populationDensity; }
    public void setPopulationDensity(Integer populationDensity) { this.populationDensity = populationDensity; }
}
