package com.jaenyeong.study_actualspringdatajpa.entity;

public interface NestedCloseProjections {
    // root (최적화가 됨)
    String getUserName();
    // 최적화 안됨
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
