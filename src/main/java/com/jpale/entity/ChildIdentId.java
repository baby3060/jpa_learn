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
public class ChildIdentId implements Serializable {
    // Child의 parent 필드와 매핑(parent_id)
    private String parent;
    private String childId;
}