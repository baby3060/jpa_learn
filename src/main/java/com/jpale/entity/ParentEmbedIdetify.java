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
public class ParentEmbedIdetify {
    @Id
    @Column(name = "parent_id")
    private String id;

    private String name;
}