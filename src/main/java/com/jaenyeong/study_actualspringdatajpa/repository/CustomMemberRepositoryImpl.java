package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {
    private final EntityManager em;

    @Override
    public List<Member> findCustomMember() {
        return em.createQuery("select m from Member m", Member.class)
            .getResultList();
    }
}
