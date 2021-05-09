package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void before() {
        memberJpaRepository.deleteAll();
    }

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

    @Test
    @DisplayName("findByUserNameAndAgeGreaterThan 쿼리 메서드 테스트")
    void findByUserNameAndAgeGreaterThan() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // Act
        final List<Member> findMembers = memberJpaRepository.findByUserNameAndAgeGreaterThan("member2", 15);

        // Assert
        final Member firstMember = findMembers.get(0);
        assertThat(firstMember.getUserName()).isEqualTo("member2");
        assertThat(firstMember.getAge()).isEqualTo(20);
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("네임드 쿼리 테스트")
    void findByUserName() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // Act
        final List<Member> findMembers = memberJpaRepository.findByUserName("member1");
        final Member findMember1 = findMembers.get(0);

        // Assert
        assertThat(findMember1).isEqualTo(member1);
    }

    @Test
    @DisplayName("페이징 테스트")
    void paging() throws Exception {
        // Arrange
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));
        memberJpaRepository.save(new Member("member7", 10));
        memberJpaRepository.save(new Member("member8", 10));
        memberJpaRepository.save(new Member("member9", 10));

        final int age = 10;
        final int offset = 1;
        final int limit = 3;

        // Act
        final List<Member> pagingMembers = memberJpaRepository.findByPage(age, offset, limit);
        final long totalCount = memberJpaRepository.totalCount(age);

        // Assert
        assertThat(pagingMembers.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(9);
    }

    @Test
    @DisplayName("벌크성 쿼리 테스트")
    void bulkUpdate() throws Exception {
        // Arrange
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 11));
        memberJpaRepository.save(new Member("member3", 12));
        memberJpaRepository.save(new Member("member4", 13));
        memberJpaRepository.save(new Member("member5", 20));
        memberJpaRepository.save(new Member("member6", 21));
        memberJpaRepository.save(new Member("member7", 22));
        memberJpaRepository.save(new Member("member8", 23));
        memberJpaRepository.save(new Member("member9", 30));

        final int age = 20;

        // Act
        final int numberOfUpdateMember = memberJpaRepository.bulkAgePlus(age);

        // Assert
        assertThat(numberOfUpdateMember).isEqualTo(5);
    }

    @Test
    @DisplayName("벌크 연산시 영속성 컨텍스트 테스트")
    void persistenceContextWhenBulkUpdate() throws Exception {
        // Arrange
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 11));
        memberJpaRepository.save(new Member("member3", 12));
        memberJpaRepository.save(new Member("member4", 13));
        memberJpaRepository.save(new Member("member5", 20));
        memberJpaRepository.save(new Member("member6", 21));
        memberJpaRepository.save(new Member("member7", 22));
        memberJpaRepository.save(new Member("member8", 23));
        memberJpaRepository.save(new Member("member9", 30));

        final int age = 20;

        // Act
        final int numberOfUpdateMember = memberJpaRepository.bulkAgePlus(age);
        final List<Member> members = memberJpaRepository.findByUserName("member9");
        final Member findMember = members.get(0);
        // Assert
        assertThat(numberOfUpdateMember).isEqualTo(5);
        assertThat(findMember.getAge()).isEqualTo(30);

        // Act
        em.flush();
        em.clear();
        final List<Member> afterMembers = memberJpaRepository.findByUserName("member9");
        final Member afterFindMember = afterMembers.get(0);
        // Assert
        assertThat(findMember.getAge()).isEqualTo(30);
        assertThat(afterFindMember.getAge()).isEqualTo(31);
    }
}

