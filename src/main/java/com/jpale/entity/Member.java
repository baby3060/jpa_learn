package com.jpale.entity;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"userName", "userAge", "password", "address", "orderList", "boardList", "team", "interestSubject"})
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@NamedQueries({
    @NamedQuery(name = "Member.notExistTeam", query = "Select m From Member m Where m.team Is Null"),
    @NamedQuery(name = "Member.countAllTeamId", query = "Select Count(m.userId) From Member m Where m.team = :team"),
    @NamedQuery(name = "Member.addressLike", query = "Select m From Member m Where m.address.city Like :city || '%' ")
})
@Table(name="MEMBER")
public class Member {
    @Id
    @Column(name="id", length = 20)
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

    @Embedded 
    private Address address;
    
    // 참조키가 Board에 존재하므로 연관관계의 주인이 아니다. 따라서 mappedBy 속성을 사용함
    @OneToMany(mappedBy = "member", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Board> boardList = new ArrayList<Board>();

    // Order에서만 Member를 신경쓰므로, mappedBy 속성 사용
    @OneToMany(mappedBy = "orderMember", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Order> orderList = new ArrayList<Order>(); 

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "interest_subject", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "subject_catal", length = 50)
    private Set<String> interestSubject = new HashSet<String>();

    @ManyToOne(fetch = FetchType.LAZY)
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