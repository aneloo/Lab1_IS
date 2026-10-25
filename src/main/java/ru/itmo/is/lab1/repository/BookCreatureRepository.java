package ru.itmo.is.lab1.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.itmo.is.lab1.model.BookCreature;
import ru.itmo.is.lab1.model.Coordinates;
import ru.itmo.is.lab1.model.MagicCity;
import ru.itmo.is.lab1.model.Ring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class BookCreatureRepository {

    private static final String FROM =
            " FROM BookCreature c JOIN c.coordinates co JOIN c.creatureLocation ci LEFT JOIN c.ring r";
    private static final String FETCH_FROM =
            " FROM BookCreature c JOIN FETCH c.coordinates co JOIN FETCH c.creatureLocation ci LEFT JOIN FETCH c.ring r";

    private static final String BATCH_HINT = "eclipselink.batch";
    private static final String BATCH_TYPE_HINT = "eclipselink.batch.type";

    @PersistenceContext(unitName = "bookCreaturesPU")
    private EntityManager em;

    public Optional<BookCreature> findById(long id) {
        return Optional.ofNullable(em.find(BookCreature.class, id));
    }

    public Optional<BookCreature> findByIdForUpdate(long id) {
        return Optional.ofNullable(em.find(BookCreature.class, id, LockModeType.PESSIMISTIC_WRITE));
    }

    public void forceVersionIncrement(BookCreature creature) {
        em.lock(creature, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
    }

    public void persist(BookCreature creature) {
        em.persist(creature);
    }

    public void remove(BookCreature creature) {
        em.remove(creature);
    }

    public void flush() {
        em.flush();
    }

    public List<BookCreature> findPage(CreatureCriteria criteria) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder jpql = new StringBuilder("SELECT c").append(FETCH_FROM).append(where(criteria, params));
        if (criteria.sortColumn() != null) {
            jpql.append(" ORDER BY ").append(criteria.sortColumn().path())
                    .append(criteria.ascending() ? " ASC" : " DESC").append(", c.id ASC");
        } else {
            jpql.append(" ORDER BY c.id ASC");
        }
        TypedQuery<BookCreature> query = em.createQuery(jpql.toString(), BookCreature.class);
        params.forEach(query::setParameter);
        return query.setFirstResult(criteria.first()).setMaxResults(criteria.maxResults()).getResultList();
    }

    public long count(CreatureCriteria criteria) {
        Map<String, Object> params = new HashMap<>();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c)" + FROM + where(criteria, params), Long.class);
        params.forEach(query::setParameter);
        return query.getSingleResult();
    }

    private static String where(CreatureCriteria criteria, Map<String, Object> params) {
        if (criteria.equalTo() == null || criteria.equalTo().isEmpty()) {
            return "";
        }
        List<String> conditions = new ArrayList<>();
        for (Map.Entry<CreatureColumn, Object> filter : criteria.equalTo().entrySet()) {
            String param = "f" + params.size();
            params.put(param, filter.getValue());
            conditions.add(filter.getKey().path() + " = :" + param);
        }
        return " WHERE " + String.join(" AND ", conditions);
    }

    public List<Integer> findAllAttackLevels() {
        return em.createQuery("SELECT c.attackLevel FROM BookCreature c WHERE c.attackLevel IS NOT NULL",
                Integer.class).getResultList();
    }

    public long countDefenseLessThan(float value) {
        return em.createQuery("SELECT COUNT(c) FROM BookCreature c WHERE c.defenseLevel < :v", Long.class)
                .setParameter("v", value).getSingleResult();
    }

    public long countDefenseGreaterThan(float value) {
        return em.createQuery("SELECT COUNT(c) FROM BookCreature c WHERE c.defenseLevel > :v", Long.class)
                .setParameter("v", value).getSingleResult();
    }

    public Optional<BookCreature> findWithStrongestRing() {
        return em.createQuery("SELECT c" + FETCH_FROM + " WHERE r IS NOT NULL ORDER BY r.weight DESC, c.id ASC",
                        BookCreature.class)
                .setMaxResults(1).getResultList().stream().findFirst();
    }

    public List<BookCreature> findByCoordinatesForUpdate(Coordinates coordinates) {
        return withRelations(em.createQuery(
                        "SELECT c FROM BookCreature c WHERE c.coordinates = :o ORDER BY c.id", BookCreature.class))
                .setParameter("o", coordinates)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
    }

    public List<BookCreature> findByCityForUpdate(MagicCity city) {
        return withRelations(em.createQuery(
                        "SELECT c FROM BookCreature c WHERE c.creatureLocation = :o ORDER BY c.id", BookCreature.class))
                .setParameter("o", city)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
    }

    public Optional<BookCreature> findByRing(Ring ring) {
        return em.createQuery("SELECT c FROM BookCreature c WHERE c.ring = :o", BookCreature.class)
                .setParameter("o", ring).getResultList().stream().findFirst();
    }

    public Optional<BookCreature> findByRingForUpdate(Ring ring) {
        return em.createQuery("SELECT c FROM BookCreature c WHERE c.ring = :o", BookCreature.class)
                .setParameter("o", ring)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList().stream().findFirst();
    }

    public Map<Long, RingOwner> findRingOwners() {
        Map<Long, RingOwner> owners = new HashMap<>();
        em.createQuery("SELECT r.id, c.id, c.name FROM BookCreature c JOIN c.ring r", Object[].class)
                .getResultList()
                .forEach(row -> owners.put((Long) row[0], new RingOwner((Long) row[1], (String) row[2])));
        return owners;
    }

    public Map<Long, Long> countGroupedByCoordinates() {
        return toCountMap(em.createQuery(
                "SELECT c.coordinates.id, COUNT(c) FROM BookCreature c GROUP BY c.coordinates.id", Object[].class)
                .getResultList());
    }

    public Map<Long, Long> countGroupedByCity() {
        return toCountMap(em.createQuery(
                "SELECT c.creatureLocation.id, COUNT(c) FROM BookCreature c GROUP BY c.creatureLocation.id",
                Object[].class).getResultList());
    }

    public Map<Long, Long> countByCoordinates(Collection<Coordinates> coordinates) {
        if (coordinates.isEmpty()) {
            return Map.of();
        }
        return toCountMap(em.createQuery("SELECT c.coordinates.id, COUNT(c) FROM BookCreature c "
                        + "WHERE c.coordinates IN :items GROUP BY c.coordinates.id", Object[].class)
                .setParameter("items", coordinates)
                .getResultList());
    }

    public Map<Long, Long> countByCities(Collection<MagicCity> cities) {
        if (cities.isEmpty()) {
            return Map.of();
        }
        return toCountMap(em.createQuery("SELECT c.creatureLocation.id, COUNT(c) FROM BookCreature c "
                        + "WHERE c.creatureLocation IN :items GROUP BY c.creatureLocation.id", Object[].class)
                .setParameter("items", cities)
                .getResultList());
    }

    private static <T> TypedQuery<T> withRelations(TypedQuery<T> query) {
        return query.setHint(BATCH_TYPE_HINT, "IN")
                .setHint(BATCH_HINT, "c.coordinates")
                .setHint(BATCH_HINT, "c.creatureLocation")
                .setHint(BATCH_HINT, "c.ring");
    }

    private static Map<Long, Long> toCountMap(List<Object[]> rows) {
        Map<Long, Long> counts = new HashMap<>();
        for (Object[] row : rows) {
            counts.put(((Number) row[0]).longValue(), ((Number) row[1]).longValue());
        }
        return counts;
    }
}
