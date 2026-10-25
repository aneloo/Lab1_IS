package ru.itmo.is.lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.itmo.is.lab1.domain.BookCreatureType;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "magic_city")
public class MagicCity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @NotBlank(message = "Название города не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull(message = "Площадь города обязательна")
    @Positive(message = "Площадь города должна быть больше 0")
    @Column(name = "area", nullable = false)
    private Double area;

    @NotNull(message = "Население обязательно")
    @Positive(message = "Население должно быть больше 0")
    @Column(name = "population", nullable = false)
    private Long population;

    @Column(name = "establishment_date")
    private LocalDate establishmentDate;

    @NotNull(message = "Правитель города обязателен")
    @Enumerated(EnumType.STRING)
    @Column(name = "governor", nullable = false, length = 16)
    private BookCreatureType governor;

    @NotNull(message = "Нужно указать, является ли город столицей")
    @Column(name = "capital", nullable = false)
    private Boolean capital;
    @Positive(message = "Плотность населения должна быть больше 0")
    @Column(name = "population_density")
    private Integer populationDensity;

    public MagicCity() {
    }

    public Long getId() { return id; }
    public long getVersion() { return version; }
    public void setId(Long id) { this.id = id; }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MagicCity c)) return false;
        return id != null && id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
