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
@IdClass(GrandChildIdentId.class)
public class GrandChildIdent {
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "parent_id"),
        @JoinColumn(name = "child_id")
    })
    private ChildItent child;

    @Id
    @Column(name = "grandchild_id")
    private String id;

    private String name;
}