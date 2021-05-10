package com.jaenyeong.study_actualspringdatajpa.controller;

import com.jaenyeong.study_actualspringdatajpa.dto.MemberDto;
import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import com.jaenyeong.study_actualspringdatajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("member" + i, i));
        }
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

    @GetMapping("/members/v1")
    public Page<Member> list(@PageableDefault(size = 5, sort = "userName", direction = Sort.Direction.DESC) final Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/members/v2")
    public Page<Member> list(@Qualifier("member") final Pageable memberPageable, @Qualifier("order") final Pageable orderPageable) {
        return memberRepository.findAll(memberPageable);
    }

    @GetMapping("/members/v3")
    public Page<MemberDto> memberDtoList(final Pageable pageable) {
        return memberRepository.findAll(pageable)
            .map(MemberDto::new);
    }
}
