package com.jpale.entity;

import lombok.*;
import javax.persistence.*;

import java.util.Date;

import java.util.List;
import java.util.ArrayList;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"createDate", "memberList"})
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "TEAM")
public class Team {

    {
        memberList = new ArrayList<Member>();
    }

    @Id
    @NonNull
    @Column(name = "team_id", length = 60)
    private String id;

    @Column(name = "TEAM_NAME", length = 60, nullable = false)
    @NonNull
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @PrePersist
    protected void orderDateInit() {
        createDate = new Date();
    }

    // 고아 객체 불허(Member의 Team을 null로 하면, Member를 삭제)
    // @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    // 고아 객체 허용
    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Member> memberList;
}