package com.jpale.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "PARENTEMBED")
public class ParentEmbedEntity {
    @EmbeddedId
    private ParentIdEmbed id;

    private String name;
}