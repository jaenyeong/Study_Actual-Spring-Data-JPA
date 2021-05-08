package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("멤버 -> EntityManager 테스트")
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

    @Test
    @DisplayName("멤버 CRUD 테스트")
    @Rollback(value = false)
    void basicCRUD() throws Exception {
        // Arrange
        final Member member1 = new Member("member1");
        final Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        /* 단건 조회 */
        // Act
        final Member findMember1 = memberJpaRepository.findById(member1.getId())
            .orElseGet(() -> new Member("Not Found1"));
        final Member findMember2 = memberJpaRepository.findById(member2.getId())
            .orElseGet(() -> new Member("Not Found2"));
        // Assert
        assertThat(findMember1.getId()).isEqualTo(member1.getId());
        assertThat(findMember2.getId()).isEqualTo(member2.getId());
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        /* 복수 조회 */
        // Act
        final List<Member> allMembers = memberJpaRepository.findAll();
        // Assert
        assertThat(allMembers.size()).isEqualTo(2);

        /* 카운트 */
        // Act
        final long numberOfMembers = memberJpaRepository.count();
        // Assert
        assertThat(numberOfMembers).isEqualTo(2);

        /* 삭제 */
        // Act
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        // Assert
        final long numberAfterDelete = memberJpaRepository.count();
        assertThat(numberAfterDelete).isEqualTo(0);
    }
}

