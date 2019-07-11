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
public class GrandChildEmbedIdentify {
    @EmbeddedId
    private GrandChildEmbeddedId id;

    // ChildEmbeddedId의 childId 컬럼과 매핑(ChildEmbeddedId)
    @MapsId("childId")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "parent_id"),
        @JoinColumn(name = "child_id")
    })
    private ChildEmbedIdentify child;

    private String name;
}