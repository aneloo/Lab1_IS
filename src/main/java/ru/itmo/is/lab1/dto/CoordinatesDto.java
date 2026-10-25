package ru.itmo.is.lab1.dto;

import java.io.Serializable;

public class CoordinatesDto implements Serializable {

    private Long id;
    private Long version;
    private Double x;
    private Float y;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }
    public Float getY() { return y; }
    public void setY(Float y) { this.y = y; }
}
