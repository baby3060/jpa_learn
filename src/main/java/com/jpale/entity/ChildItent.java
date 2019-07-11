package com.jpale.entity;

import javax.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(ChildIdentId.class)
public class ChildItent {
    @Id
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ParentIdent parent;

    @Id
    @Column(name = "child_id")
    private String childId;

    private String name;
}