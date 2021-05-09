package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.dto.MemberDto;
import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import com.jaenyeong.study_actualspringdatajpa.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void before() {
        memberRepository.deleteAll();
        teamRepository.deleteAll();
    }

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

    @Test
    @DisplayName("페이징 테스트")
    void page() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));

        final int age = 10;

        final int startPage = 0;
        final int pageSize = 3;
        final PageRequest pageRequest = PageRequest.of(startPage, pageSize, Sort.by(Sort.Direction.DESC, "userName"));

        // Act
        final Page<Member> membersPage = memberRepository.findPageByAge(age, pageRequest);
        final List<Member> content = membersPage.getContent();

        // Assert
        assertThat(content.size()).isEqualTo(3);
        assertThat(membersPage.getTotalElements()).isEqualTo(9);
        assertThat(membersPage.getNumber()).isEqualTo(0);
        assertThat(membersPage.getTotalPages()).isEqualTo(3);
        assertThat(membersPage.isFirst()).isTrue();
        assertThat(membersPage.hasNext()).isTrue();
    }

    @Test
    @DisplayName("슬라이싱 테스트")
    void slice() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));

        final int age = 10;

        final int startPage = 0;
        final int pageSize = 3;
        final PageRequest pageRequest = PageRequest.of(startPage, pageSize, Sort.by(Sort.Direction.DESC, "userName"));

        // Act
        final Slice<Member> membersSlice = memberRepository.findSliceByAge(age, pageRequest);

        // Assert
        assertThat(membersSlice.getNumber()).isEqualTo(0);
        assertThat(membersSlice.isFirst()).isTrue();
        assertThat(membersSlice.hasNext()).isTrue();
    }

    @Test
    @DisplayName("리스트 테스트")
    void list() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));

        final int age = 10;

        final int startPage = 0;
        final int pageSize = 3;
        final PageRequest pageRequest = PageRequest.of(startPage, pageSize, Sort.by(Sort.Direction.DESC, "userName"));

        // Act
        final List<Member> members = memberRepository.findListByAge(age, pageRequest);

        // Assert
        assertThat(members.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("count 분리 테스트")
    void separateCountQuery() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));

        final int startPage = 0;
        final int pageSize = 3;
        final PageRequest pageRequest = PageRequest.of(startPage, pageSize, Sort.by(Sort.Direction.DESC, "userName"));

        // Act
        final Page<Member> membersPage = memberRepository.findMemberAllCountBy(pageRequest);
        final List<Member> content = membersPage.getContent();

        // Assert
        assertThat(content.size()).isEqualTo(3);
        assertThat(membersPage.getTotalElements()).isEqualTo(9);
        assertThat(membersPage.getNumber()).isEqualTo(0);
        assertThat(membersPage.getTotalPages()).isEqualTo(3);
        assertThat(membersPage.isFirst()).isTrue();
        assertThat(membersPage.hasNext()).isTrue();
    }

    @Test
    @DisplayName("dto 변환 테스트")
    void pageDto() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));
        memberRepository.save(new Member("member7", 10));
        memberRepository.save(new Member("member8", 10));
        memberRepository.save(new Member("member9", 10));

        final int age = 10;

        final int startPage = 0;
        final int pageSize = 3;
        final PageRequest pageRequest = PageRequest.of(startPage, pageSize, Sort.by(Sort.Direction.DESC, "userName"));

        // Act
        final Page<Member> membersPage = memberRepository.findPageByAge(age, pageRequest);
        final Page<MemberDto> memberDtos = membersPage.map(member -> new MemberDto(member.getId(), member.getUserName(), "test team name"));

        final List<Member> content = membersPage.getContent();

        // Assert
        assertThat(content.size()).isEqualTo(3);
        assertThat(memberDtos.getTotalElements()).isEqualTo(9);
        assertThat(memberDtos.getNumber()).isEqualTo(0);
        assertThat(memberDtos.getTotalPages()).isEqualTo(3);
        assertThat(memberDtos.isFirst()).isTrue();
        assertThat(memberDtos.hasNext()).isTrue();
    }

    @Test
    @DisplayName("벌크성 쿼리 테스트")
    void bulkUpdate() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 11));
        memberRepository.save(new Member("member3", 12));
        memberRepository.save(new Member("member4", 13));
        memberRepository.save(new Member("member5", 20));
        memberRepository.save(new Member("member6", 21));
        memberRepository.save(new Member("member7", 22));
        memberRepository.save(new Member("member8", 23));
        memberRepository.save(new Member("member9", 30));

        final List<Member> members = memberRepository.findByUserName("member9");

        final int age = 20;

        // Act
        final int numberOfUpdateMember = memberRepository.bulkAgePlus(age);

        // Assert
        assertThat(numberOfUpdateMember).isEqualTo(5);
    }

    @Test
    @DisplayName("벌크 연산시 영속성 컨텍스트 테스트")
    void persistenceContextWhenBulkUpdate() throws Exception {
        // Arrange
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 11));
        memberRepository.save(new Member("member3", 12));
        memberRepository.save(new Member("member4", 13));
        memberRepository.save(new Member("member5", 20));
        memberRepository.save(new Member("member6", 21));
        memberRepository.save(new Member("member7", 22));
        memberRepository.save(new Member("member8", 23));
        memberRepository.save(new Member("member9", 30));

        final int age = 20;

        // Act
        final List<Member> members = memberRepository.findByUserName("member9");
        final Member findMember = members.get(0);
        // Assert
        assertThat(findMember.getAge()).isEqualTo(30);

        // Act
        final int numberOfUpdateMember = memberRepository.bulkAgePlus(age);
        final List<Member> afterMembers = memberRepository.findByUserName("member9");
        final Member afterFindMember = afterMembers.get(0);
        // Assert
        assertThat(numberOfUpdateMember).isEqualTo(5);
        assertThat(findMember.getAge()).isEqualTo(30);
        assertThat(afterFindMember.getAge()).isEqualTo(31);
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    void teamLazyLoading() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        // Act
        em.flush();
        em.clear();

        final List<Member> members = memberRepository.findAll();
        final Member findMember = members.get(0);

        // Assert
        assertThat(findMember.getUserName()).isEqualTo("member1");
        assertThat(findMember.getTeam().getClass().getName()).contains("Proxy");
        assertThat(findMember.getTeam().getName()).contains("teamA");
    }

    @Test
    @DisplayName("페치 조인 테스트")
    void teamFetchJoin() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        // Act
        em.flush();
        em.clear();

        final List<Member> members = memberRepository.findFetchJoin();
        final Member findMember = members.get(0);

        // Assert
        assertThat(findMember.getUserName()).isEqualTo("member1");
        assertThat(findMember.getTeam().getClass().getName()).doesNotContain("Proxy");
        assertThat(findMember.getTeam().getName()).contains("teamA");
    }

    @Test
    @DisplayName("엔티티 그래프 테스트")
    void teamEntityGraph() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));

        // Act
        em.flush();
        em.clear();

        final List<Member> members = memberRepository.findMemberEntityGraph();
        final Member findMember = members.get(0);

        // Assert
        assertThat(findMember.getUserName()).isEqualTo("member1");
        // @EntityGraph(attributePaths = {"team"}) 를 사용하면 fetch join과 동일
        assertThat(findMember.getTeam().getClass().getName()).doesNotContain("Proxy");
        assertThat(findMember.getTeam().getName()).contains("teamA");
    }

    @Test
    @DisplayName("쿼리 메서드에 엔티티 그래프 테스트")
    void teamQueryMethodEntityGraph() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        em.flush();
        em.clear();

        final List<Member> members = memberRepository.findEntityGraphByUserName("member1");
        final Member findMember = members.get(0);

        // Assert
        assertThat(findMember.getUserName()).isEqualTo("member1");
        // @EntityGraph(attributePaths = {"team"}) 를 사용하면 fetch join과 동일
        assertThat(findMember.getTeam().getClass().getName()).doesNotContain("Proxy");
        assertThat(findMember.getTeam().getName()).contains("teamA");

        assertThat(members).hasSize(1);
        assertThat(findMember.getId()).isEqualTo(member1.getId());
    }

    @Test
    @DisplayName("쿼리 메서드에 네임드 엔티티 그래프 테스트")
    void teamQueryMethodNamedEntityGraph() throws Exception {
        // Arrange
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        em.flush();
        em.clear();

        final List<Member> members = memberRepository.findNamedEntityGraphByUserName("member1");
        final Member findMember = members.get(0);

        // Assert
        assertThat(findMember.getUserName()).isEqualTo("member1");
        // @EntityGraph(attributePaths = {"team"})를 사용하면 fetch join과 동일
        assertThat(findMember.getTeam().getClass().getName()).doesNotContain("Proxy");
        assertThat(findMember.getTeam().getName()).contains("teamA");

        assertThat(members).hasSize(1);
        assertThat(findMember.getId()).isEqualTo(member1.getId());
    }
}
