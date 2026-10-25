package ru.itmo.is.lab1.dto;

import ru.itmo.is.lab1.domain.BookCreatureType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BookCreatureDto implements Serializable {

    private Long id;
    private Long version;
    private String name;
    private LocalDateTime creationDate;
    private Integer age;
    private BookCreatureType creatureType;
    private Integer attackLevel;
    private Float defenseLevel;
    private CoordinatesDto coordinates;
    private MagicCityDto creatureLocation;
    private RingDto ring;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public BookCreatureType getCreatureType() { return creatureType; }
    public void setCreatureType(BookCreatureType creatureType) { this.creatureType = creatureType; }
    public Integer getAttackLevel() { return attackLevel; }
    public void setAttackLevel(Integer attackLevel) { this.attackLevel = attackLevel; }
    public Float getDefenseLevel() { return defenseLevel; }
    public void setDefenseLevel(Float defenseLevel) { this.defenseLevel = defenseLevel; }
    public CoordinatesDto getCoordinates() { return coordinates; }
    public void setCoordinates(CoordinatesDto coordinates) { this.coordinates = coordinates; }
    public MagicCityDto getCreatureLocation() { return creatureLocation; }
    public void setCreatureLocation(MagicCityDto creatureLocation) { this.creatureLocation = creatureLocation; }
    public RingDto getRing() { return ring; }
    public void setRing(RingDto ring) { this.ring = ring; }
}
