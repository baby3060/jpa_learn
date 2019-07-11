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
public class GrandChildIdentId implements Serializable {
    private ChildIdentId child;
    private String id;
}