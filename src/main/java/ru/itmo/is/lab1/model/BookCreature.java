package ru.itmo.is.lab1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.itmo.is.lab1.domain.BookCreatureType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_creature")
public class BookCreature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @Valid
    @NotNull(message = "Координаты обязательны")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @NotNull(message = "Дата создания обязательна")
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @Positive(message = "Возраст должен быть больше 0")
    @Column(name = "age", nullable = false)
    private int age;

    @NotNull(message = "Тип существа обязателен")
    @Enumerated(EnumType.STRING)
    @Column(name = "creature_type", nullable = false, length = 16)
    private BookCreatureType creatureType;

    @Valid
    @NotNull(message = "Город обязателен")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "creature_location_id", nullable = false)
    private MagicCity creatureLocation;

    @Positive(message = "Уровень атаки должен быть больше 0")
    @Column(name = "attack_level")
    private Integer attackLevel;

    @Positive(message = "Уровень защиты должен быть больше 0")
    @Column(name = "defense_level")
    private Float defenseLevel;

    @Valid
    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ring_id", unique = true)
    private Ring ring;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public BookCreatureType getCreatureType() { return creatureType; }
    public void setCreatureType(BookCreatureType creatureType) { this.creatureType = creatureType; }
    public MagicCity getCreatureLocation() { return creatureLocation; }
    public void setCreatureLocation(MagicCity creatureLocation) { this.creatureLocation = creatureLocation; }
    public Integer getAttackLevel() { return attackLevel; }
    public void setAttackLevel(Integer attackLevel) { this.attackLevel = attackLevel; }
    public Float getDefenseLevel() { return defenseLevel; }
    public void setDefenseLevel(Float defenseLevel) { this.defenseLevel = defenseLevel; }
    public Ring getRing() { return ring; }
    public void setRing(Ring ring) { this.ring = ring; }
    public long getVersion() { return version; }
    public void setVersion(long version) { this.version = version; }
}
