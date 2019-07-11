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
public class GrandChildEmbeddedId implements Serializable {
    
    private ChildEmbeddedId childId;

    @Column(name = "grandchild_id")
    private String id;
}