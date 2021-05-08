package com.jaenyeong.study_actualspringdatajpa.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
// ToString 안에 team 필드 삽입하고 Team클래스의 toString에도 members 필드를 삽입하게 되면 무한루프 발생하게 되니 주의
// 따라서 가급적 연관관계 필드는 넣지 않는 것이 좋음
@ToString(of = {"id", "userName", "age"})
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;
    private int age;

    // *ToOne 관계는 기본적으로 EAGER(즉시 로딩)이기 때문에 LAZY(지연 로딩)으로 변경
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(final String userName) {
        this.userName = userName;
    }

    public Member(final String userName, final int age, final Team team) {
        this.userName = userName;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(final Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
