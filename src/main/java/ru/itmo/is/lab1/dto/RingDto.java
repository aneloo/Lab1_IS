package ru.itmo.is.lab1.dto;

import java.io.Serializable;

public class RingDto implements Serializable {

    private Long id;
    private Long version;
    private String name;
    private Integer weight;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
}
