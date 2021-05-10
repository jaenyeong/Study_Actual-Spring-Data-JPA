package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.dto.MemberDto;
import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, CustomMemberRepository {
    List<Member> findByUserNameAndAgeGreaterThan(final String userName, final int age);

    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();

    List<Member> findByUserName(@Param("userName") final String userName);

    @Query("select m from Member m where m.userName = :userName and m.age = :age")
    List<Member> findUserByQuery(@Param("userName") final String userName, @Param("age") final int age);

    @Query("select m.userName from Member m")
    List<String> findUserNameList();

    @Query("select new com.jaenyeong.study_actualspringdatajpa.dto.MemberDto(m.id, m.userName, t.name) from Member m join m.team t")
    List<MemberDto> findUsersForMemberDto();

    @Query("select m from Member m where m.userName in :userNames")
    List<Member> findByUserNames(@Param("userNames") Collection<String> userNames);

    List<Member> findListByUserName(final String userName);

    Member findMemberByUserName(final String userName);

    Optional<Member> findOptionalByUserName(final String userName);

    Page<Member> findPageByAge(final int age, final Pageable pageable);

    Slice<Member> findSliceByAge(final int age, final Pageable pageable);

    List<Member> findListByAge(final int age, final Pageable pageable);

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.userName) from Member m")
    Page<Member> findMemberAllCountBy(final Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") final int age);

    @Query("select m from Member m left join fetch m.team")
    List<Member> findFetchJoin();

    @EntityGraph(attributePaths = "team")
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = "team")
    List<Member> findEntityGraphByUserName(@Param("userName") final String userName);

    @EntityGraph("Member.all")
    List<Member> findNamedEntityGraphByUserName(@Param("userName") final String userName);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Optional<Member> findReadOnlyById(final Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUserName(final String userName);
}
