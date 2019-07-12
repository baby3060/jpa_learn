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

    @OneToMany(mappedBy = "team", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Member> memberList;
}