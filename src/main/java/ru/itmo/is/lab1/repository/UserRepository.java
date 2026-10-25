package ru.itmo.is.lab1.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.itmo.is.lab1.model.AppUser;

import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "bookCreaturesPU")
    private EntityManager em;

    public Optional<AppUser> findByUsername(String username) {
        return em.createQuery("SELECT u FROM AppUser u WHERE u.username = :u", AppUser.class)
                .setParameter("u", username).getResultList().stream().findFirst();
    }

    public void persist(AppUser user) {
        em.persist(user);
        em.flush();
    }
}
