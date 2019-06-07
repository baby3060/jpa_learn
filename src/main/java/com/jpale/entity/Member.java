package com.jpale.entity;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"userName", "userAge", "password", "street", "zipcode", "city", "team"})
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="MEMBER")
public class Member {
    @Id
    @Column(name="ID", length = 20)
    @NonNull
    private String userId;

    @Column(name="name", length = 40, nullable = false)
    @NonNull
    private String userName;

    @Column(name="age", precision = 3, scale = 0, nullable = false)
    @NonNull
    private Integer userAge;

    @NonNull
    @Column(length = 60, nullable = false)
    // db 컬럼명과 동일하므로, 생략
    private String password;

    @Column(length = 60)
    private String city;
    @Column(length = 60)
    private String street;
    @Column(length = 60)
    private String zipcode;

    // 참조키가 Board에 존재하므로 연관관계의 주인이 아니다. 따라서 mappedBy 속성을 사용함
    @OneToMany(mappedBy = "member")
    private List<Board> boardList;

    // Order에서만 Member를 신경쓰므로, mappedBy 속성 사용
    @OneToMany(mappedBy = "orderMember")
    private List<Order> orderList;

    {
        boardList = new ArrayList<Board>();
        orderList = new ArrayList<Order>();
    }

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    public void setTeam(Team team) {
        // 초기화
        if( this.team != null ) {
            this.team.getMemberList().remove(this);
        }
        // 할당
        this.team = team;
        // List 할당
        if( team != null ) {
            team.getMemberList().add(this);
        }
    }
}