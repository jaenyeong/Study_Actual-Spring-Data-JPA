package com.jaenyeong.study_actualspringdatajpa.repository;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;

import java.util.List;

public interface CustomMemberRepository {
    List<Member> findCustomMember();
}
