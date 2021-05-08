package com.jaenyeong.study_actualspringdatajpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {
    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("멤버 테스트")
    @Rollback(value = false)
    void testEntity() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 20, teamA);
        final Member member3 = new Member("member3", 30, teamB);
        final Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 강제로 DB에 쿼리 전송
        em.flush();
        // 영속성 컨텍스트 캐시를 비움
        em.clear();

        // Act
        final List<Member> members = em.createQuery("select m from Member m", Member.class)
            .getResultList();

        // Assert
        for (Member member : members) {
            System.out.println("member : " + member);
            System.out.println("member.team : " + member.getTeam());
        }
    }
}
