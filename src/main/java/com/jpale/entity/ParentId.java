package com.jpale.entity;

import java.io.Serializable;
import lombok.*;


@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ParentId implements Serializable {
    private static final long serialVersionUID = 12003012331L;

    private String id1;
    private String id2;
}