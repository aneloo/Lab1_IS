package ru.itmo.is.lab1.web.form;

import ru.itmo.is.lab1.dto.BookCreatureDto;
import ru.itmo.is.lab1.dto.CoordinatesDto;
import ru.itmo.is.lab1.dto.LinkMode;
import ru.itmo.is.lab1.dto.MagicCityDto;
import ru.itmo.is.lab1.dto.RingDto;

import java.io.Serializable;
import java.util.List;

public class CreatureForm implements Serializable {

    private BookCreatureDto creature = new BookCreatureDto();

    private LinkMode coordinatesMode = LinkMode.NEW;
    private Long coordinatesId;
    private CoordinatesDto coordinates = new CoordinatesDto();

    private LinkMode cityMode = LinkMode.NEW;
    private Long cityId;
    private MagicCityDto city = new MagicCityDto();

    private LinkMode ringMode = LinkMode.NONE;
    private Long ringId;
    private RingDto ring = new RingDto();

    private List<CoordinatesDto> existingCoordinates = List.of();
    private List<MagicCityDto> existingCities = List.of();
    private List<RingDto> existingRings = List.of();

    public void setOptions(List<CoordinatesDto> coordinates, List<MagicCityDto> cities, List<RingDto> rings) {
        this.existingCoordinates = coordinates;
        this.existingCities = cities;
        this.existingRings = rings;
    }

    public boolean showFields(LinkMode mode) {
        return mode == LinkMode.NEW || mode == LinkMode.CURRENT;
    }

    public BookCreatureDto getCreature() { return creature; }
    public void setCreature(BookCreatureDto creature) { this.creature = creature; }
    public LinkMode getCoordinatesMode() { return coordinatesMode; }
    public void setCoordinatesMode(LinkMode coordinatesMode) { this.coordinatesMode = coordinatesMode; }
    public Long getCoordinatesId() { return coordinatesId; }
    public void setCoordinatesId(Long coordinatesId) { this.coordinatesId = coordinatesId; }
    public CoordinatesDto getCoordinates() { return coordinates; }
    public void setCoordinates(CoordinatesDto coordinates) { this.coordinates = coordinates; }
    public LinkMode getCityMode() { return cityMode; }
    public void setCityMode(LinkMode cityMode) { this.cityMode = cityMode; }
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    public MagicCityDto getCity() { return city; }
    public void setCity(MagicCityDto city) { this.city = city; }
    public LinkMode getRingMode() { return ringMode; }
    public void setRingMode(LinkMode ringMode) { this.ringMode = ringMode; }
    public Long getRingId() { return ringId; }
    public void setRingId(Long ringId) { this.ringId = ringId; }
    public RingDto getRing() { return ring; }
    public void setRing(RingDto ring) { this.ring = ring; }
    public List<CoordinatesDto> getExistingCoordinates() { return existingCoordinates; }
    public List<MagicCityDto> getExistingCities() { return existingCities; }
    public List<RingDto> getExistingRings() { return existingRings; }
}
