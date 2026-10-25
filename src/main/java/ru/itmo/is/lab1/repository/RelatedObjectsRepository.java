package ru.itmo.is.lab1.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.LockModeType;
import ru.itmo.is.lab1.model.Coordinates;
import ru.itmo.is.lab1.model.MagicCity;
import ru.itmo.is.lab1.model.Ring;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RelatedObjectsRepository {

    @PersistenceContext(unitName = "bookCreaturesPU")
    private EntityManager em;

    public Optional<Coordinates> findCoordinates(long id) {
        return Optional.ofNullable(em.find(Coordinates.class, id));
    }

    public Optional<Coordinates> findCoordinatesForUpdate(long id) {
        return Optional.ofNullable(em.find(Coordinates.class, id, LockModeType.PESSIMISTIC_WRITE));
    }

    public Optional<MagicCity> findCity(long id) {
        return Optional.ofNullable(em.find(MagicCity.class, id));
    }

    public Optional<MagicCity> findCityForUpdate(long id) {
        return Optional.ofNullable(em.find(MagicCity.class, id, LockModeType.PESSIMISTIC_WRITE));
    }

    public Optional<Ring> findRing(long id) {
        return Optional.ofNullable(em.find(Ring.class, id));
    }

    public Optional<Ring> findRingForUpdate(long id) {
        return Optional.ofNullable(em.find(Ring.class, id, LockModeType.PESSIMISTIC_WRITE));
    }

    public void lockCoordinates(Collection<Coordinates> coordinates) {
        if (!coordinates.isEmpty()) {
            em.createQuery("SELECT o FROM Coordinates o WHERE o.id IN :ids ORDER BY o.id", Coordinates.class)
                    .setParameter("ids", coordinates.stream().map(Coordinates::getId).toList())
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
        }
    }

    public void lockCities(Collection<MagicCity> cities) {
        if (!cities.isEmpty()) {
            em.createQuery("SELECT o FROM MagicCity o WHERE o.id IN :ids ORDER BY o.id", MagicCity.class)
                    .setParameter("ids", cities.stream().map(MagicCity::getId).toList())
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
        }
    }

    public boolean isManaged(Object entity) {
        return em.contains(entity);
    }

    public List<Coordinates> findAllCoordinates() {
        return em.createQuery("SELECT o FROM Coordinates o ORDER BY o.id", Coordinates.class).getResultList();
    }

    public List<MagicCity> findAllCities() {
        return em.createQuery("SELECT o FROM MagicCity o ORDER BY o.id", MagicCity.class).getResultList();
    }

    public List<Ring> findAllRings() {
        return em.createQuery("SELECT o FROM Ring o ORDER BY o.id", Ring.class).getResultList();
    }

    public List<Ring> findFreeRings() {
        return em.createQuery("SELECT r FROM Ring r WHERE NOT EXISTS "
                + "(SELECT c FROM BookCreature c WHERE c.ring = r) ORDER BY r.id", Ring.class).getResultList();
    }

    public void persist(Object entity) {
        em.persist(entity);
    }

    public void remove(Object entity) {
        em.remove(entity);
    }

    public void flush() {
        em.flush();
    }
}
