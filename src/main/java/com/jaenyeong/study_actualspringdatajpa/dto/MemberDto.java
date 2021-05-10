package com.jaenyeong.study_actualspringdatajpa.dto;

import com.jaenyeong.study_actualspringdatajpa.entity.Member;
import com.jaenyeong.study_actualspringdatajpa.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String userName;
    private String teamName;

    public MemberDto(final Member member) {
        this.id = member.getId();
        this.userName = member.getUserName();
        final Team team = member.getTeam();
        if (team != null) {
            this.teamName = team.getName();
        }
    }
}
