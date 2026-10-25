package ru.itmo.is.lab1.repository;

public enum CreatureColumn {

    ID("c.id"),
    NAME("c.name"),
    X("co.x"),
    Y("co.y"),
    CREATION_DATE("c.creationDate"),
    AGE("c.age"),
    CREATURE_TYPE("c.creatureType"),
    CITY_NAME("ci.name"),
    ATTACK_LEVEL("c.attackLevel"),
    DEFENSE_LEVEL("c.defenseLevel"),
    RING_NAME("r.name"),
    RING_WEIGHT("r.weight");

    private final String path;

    CreatureColumn(String path) {
        this.path = path;
    }

    String path() {
        return path;
    }
}
