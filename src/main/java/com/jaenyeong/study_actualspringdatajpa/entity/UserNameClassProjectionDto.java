package com.jaenyeong.study_actualspringdatajpa.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserNameClassProjectionDto {
    // 생성자 파라미터명으로 매칭됨
    private final String userName;
}
