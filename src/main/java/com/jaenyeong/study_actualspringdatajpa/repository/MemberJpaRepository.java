package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Member save(final Member member) {
        em.persist(member);
        return member;
    }

    public void delete(final Member member) {
        em.remove(member);
    }

    public Member find(final Long id) {
        return em.find(Member.class, id);
    }

    public Optional<Member> findById(final Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
            .getResultList();
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
            .getSingleResult();
    }

    public List<Member> findByUserNameAndAgeGreaterThan(final String userName, final int age) {
        return em.createQuery("select m from Member m where m.userName = :userName and m.age > :age", Member.class)
            .setParameter("userName", userName)
            .setParameter("age", age)
            .getResultList();
    }

    public List<Member> findByUserName(final String userName) {
        return em.createNamedQuery("Member.findByUserName", Member.class)
            .setParameter("userName", userName)
            .getResultList();
    }

    public List<Member> findByPage(final int age, final int offset, final int limit) {
        return em.createQuery("select m from Member m where m.age = :age order by m.userName desc", Member.class)
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
    }

    public long totalCount(final int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
            .setParameter("age", age)
            .getSingleResult();
    }

    public int bulkAgePlus(final int age) {
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
            .setParameter("age", age)
            .executeUpdate();
    }

    public void deleteAll() {
        em.createQuery("delete from Member m")
            .executeUpdate();
    }
}
