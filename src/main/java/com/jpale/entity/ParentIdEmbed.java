package com.jpale.entity;

import javax.persistence.*;
import java.io.Serializable;

import lombok.*;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ParentIdEmbed implements Serializable {
    private static final long serialVersionUID = 10000L;

    @Column(name = "parent_emdid1")
    private String id1;

    @Column(name = "parent_emdid2")
    private String id2;

}