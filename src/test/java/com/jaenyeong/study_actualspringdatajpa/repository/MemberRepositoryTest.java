package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.dto.MemberDto;
import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import com.jaenyeong.study_actualspringdatajpa.entity.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("멤버 -> Spring-Data-JPA 테스트")
@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    @DisplayName("회원 테스트")
    // 데이터 확인을 위해 추가
    @Rollback(value = false)
    void testMember() throws Exception {
        // Arrange
        final Member memberA = new Member("memberA");

        // Act
        final Member savedMember = memberRepository.save(memberA);
        final Member findMember = memberRepository.findById(savedMember.getId())
            .orElseGet(() -> new Member("memberB"));

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
        memberRepository.save(member1);
        memberRepository.save(member2);

        /* 단건 조회 */
        // Act
        final Member findMember1 = memberRepository.findById(member1.getId())
            .orElseGet(() -> new Member("Not Found1"));
        final Member findMember2 = memberRepository.findById(member2.getId())
            .orElseGet(() -> new Member("Not Found2"));
        // Assert
        assertThat(findMember1.getId()).isEqualTo(member1.getId());
        assertThat(findMember2.getId()).isEqualTo(member2.getId());
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        /* 복수 조회 */
        // Act
        final List<Member> allMembers = memberRepository.findAll();
        // Assert
        assertThat(allMembers.size()).isEqualTo(2);

        /* 카운트 */
        // Act
        final long numberOfMembers = memberRepository.count();
        // Assert
        assertThat(numberOfMembers).isEqualTo(2);

        /* 삭제 */
        // Act
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        // Assert
        final long numberAfterDelete = memberRepository.count();
        assertThat(numberAfterDelete).isEqualTo(0);
    }

    @Test
    @DisplayName("findByUserNameAndAgeGreaterThan 쿼리 메서드 테스트")
    void findByUserNameAndAgeGreaterThan() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        final List<Member> findMembers = memberRepository.findByUserNameAndAgeGreaterThan("member2", 15);

        // Assert
        final Member firstMember = findMembers.get(0);
        assertThat(firstMember.getUserName()).isEqualTo("member2");
        assertThat(firstMember.getAge()).isEqualTo(20);
        assertThat(findMembers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿼리 메서드 작성시 By 조건 테스트")
    void findHelloBy() throws Exception {
        // Act
        final List<Member> helloMembers = memberRepository.findHelloBy();

        // Assert
        assertThat(helloMembers.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("쿼리 메서드 작성시 Top3 조건 테스트")
    void finTop3HelloBy() throws Exception {
        // Act
        final List<Member> helloMembers = memberRepository.findTop3HelloBy();

        // Assert
        assertThat(helloMembers.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("네임드 쿼리 테스트")
    void findByUserName() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        final List<Member> findMembers = memberRepository.findByUserName("member1");
        final Member findMember1 = findMembers.get(0);

        // Assert
        assertThat(findMember1).isEqualTo(member1);
    }

    @Test
    @DisplayName("@Query JPQL 테스트")
    void findUser() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        final List<Member> findMembers = memberRepository.findUserByQuery("member1", 10);
        final Member findMember1 = findMembers.get(0);

        // Assert
        assertThat(findMember1).isEqualTo(member1);
    }

    @Test
    @DisplayName("값 형식 조회 쿼리 테스트")
    void findUserNameList() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        final List<String> userNameList = memberRepository.findUserNameList();
        final String findMember1Name = userNameList.get(0);
        final String findMember2Name = userNameList.get(1);

        // Assert
        assertThat(findMember1Name).isEqualTo(member1.getUserName());
        assertThat(findMember2Name).isEqualTo(member2.getUserName());
    }

    @Test
    @DisplayName("DTO를 사용한 조회 쿼리 테스트")
    void findUsersForMemberDto() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        final Member member1 = new Member("member1", 10);
        member1.setTeam(teamA);
        memberRepository.save(member1);

        // Act
        final List<MemberDto> findMembers = memberRepository.findUsersForMemberDto();
        final MemberDto findMember1 = findMembers.get(0);

        // Assert
        assertThat(findMember1.getId()).isEqualTo(member1.getId());
        assertThat(findMember1.getUserName()).isEqualTo(member1.getUserName());
        assertThat(findMember1.getTeamName()).isEqualTo(member1.getTeam().getName());
    }

    @Test
    @DisplayName("컬렉션 파라미터 바인딩 쿼리 테스트")
    void findByNames() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        final List<Member> userNameList = memberRepository.findByUserNames(Arrays.asList("member1", "member2"));
        final Member findMember1 = userNameList.get(0);
        final Member findMember2 = userNameList.get(1);

        // Assert
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
    }

    @Test
    @DisplayName("쿼리별 반환 타입 테스트")
    void queryReturnType() throws Exception {
        // Arrange
        final Member member1 = new Member("member1", 10);
        final Member member2 = new Member("member2", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        final List<Member> findMembers = memberRepository.findListByUserName("member1");
        final Member findMember1 = memberRepository.findMemberByUserName("member1");
        final Member optionalMember1 = memberRepository.findOptionalByUserName("member1")
            .orElseGet(() -> new Member("Not Found", 99));

        // Assert
        assertThat(findMembers.get(0)).isEqualTo(member1);
        assertThat(findMember1).isEqualTo(member1);
        assertThat(optionalMember1).isEqualTo(member1);
    }
}
