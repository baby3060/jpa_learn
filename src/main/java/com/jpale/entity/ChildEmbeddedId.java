package com.jpale.entity;

import java.io.Serializable;

import javax.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Embeddable
public class ChildEmbeddedId implements Serializable {
    private String parentId;

    @Column(name = "child_id")
    private String id;
}