package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
// JPA 내에 모든 데이터 변경은 트랜잭션 내에서 처리되어야 함
// 트랜잭션 내에서 EntityManager 사용 가능
// 테스트의 트랜잭션은 자동으로 롤백처리가 됨
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("회원 테스트")
    // 데이터 확인을 위해 추가
    @Rollback(value = false)
    void testMember() throws Exception {
        // Arrange
        final Member memberA = new Member("memberA");

        // Act
        final Member savedMember = memberJpaRepository.save(memberA);
        final Member findMember = memberJpaRepository.find(savedMember.getId());

        // Assert
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUserName()).isEqualTo(savedMember.getUserName());
        assertThat(findMember).isEqualTo(savedMember);
    }
}

