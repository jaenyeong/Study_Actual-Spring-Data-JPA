package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserNameAndAgeGreaterThan(final String userName, final int age);

    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();

    List<Member> findByUserName(@Param("userName") final String userName);

    @Query("select m from Member m where m.userName = :userName and m.age = :age")
    List<Member> findUserByQuery(@Param("userName") final String userName, @Param("age") final int age);
}
