package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomMemberQueryRepository {
    private final EntityManager em;

    List<Member> findAllMembers() {
        return em.createQuery("select m from Member m", Member.class)
            .getResultList();
    }
}
