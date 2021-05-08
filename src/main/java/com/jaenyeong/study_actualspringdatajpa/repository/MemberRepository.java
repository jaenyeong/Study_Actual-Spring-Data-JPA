package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserNameAndAgeGreaterThan(final String userName, final int age);

    List<Member> findHelloBy();

    List<Member> findTop3HelloBy();
}
