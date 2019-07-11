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
public class ChildEmbedIdentify {
    @EmbeddedId
    private ChildEmbeddedId id;

    // ChildEmbeddedId의 parentId 컬럼과 매핑(문자열)
    @MapsId("parentId")
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ParentEmbedIdetify parent;

    private String name;
}