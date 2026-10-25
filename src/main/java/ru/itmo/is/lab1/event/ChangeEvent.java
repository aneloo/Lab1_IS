package ru.itmo.is.lab1.event;

public record ChangeEvent(Type type, Long objectId, Long secondId, Integer affected, String name, String actor) {

    public enum Type {
        CREATURE_CREATED,
        CREATURE_UPDATED,
        CREATURE_DELETED,
        RINGS_SWAPPED,
        COORDINATES_DELETED,
        CITY_DELETED,
        RING_DELETED
    }

    public static ChangeEvent creature(Type type, long id, String name, String actor) {
        return new ChangeEvent(type, id, null, null, name, actor);
    }

    public static ChangeEvent ringsSwapped(long firstId, long secondId, String actor) {
        return new ChangeEvent(Type.RINGS_SWAPPED, firstId, secondId, null, null, actor);
    }

    public static ChangeEvent relatedDeleted(Type type, long id, int affected, String actor) {
        return new ChangeEvent(type, id, null, affected, null, actor);
    }
}
