package com.jaenyeong.study_actualspringdatajpa.entity;

import org.springframework.beans.factory.annotation.Value;

public interface UserNameOpenProjection {
    @Value("#{target.userName + ' ' + target.age + ' ' + target.team.name}")
    String getUserName();
}
