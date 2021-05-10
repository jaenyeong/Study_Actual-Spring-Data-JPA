package com.jaenyeong.study_actualspringdatajpa.controller;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import com.jaenyeong.study_actualspringdatajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("member1", 10));
    }

    @GetMapping("/members/v1/{id}")
    public String findMemberV1(@PathVariable("id") final Long id) {
        return memberRepository.findById(id)
            .orElseGet(() -> new Member("Not Found", 99))
            .getUserName();
    }

    @GetMapping("/members/v2/{id}")
    public String findMemberV2(@PathVariable("id") final Member member) {
        return member.getUserName();
    }
}
