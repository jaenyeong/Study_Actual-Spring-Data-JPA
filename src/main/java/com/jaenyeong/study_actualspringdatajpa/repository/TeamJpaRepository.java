package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Team save(final Team team) {
        em.persist(team);
        return team;
    }

    public void delete(final Team team) {
        em.remove(team);
    }

    public Team find(final Long id) {
        return em.find(Team.class, id);
    }

    public Optional<Team> findById(final Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
            .getResultList();
    }

    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
            .getSingleResult();
    }
}
