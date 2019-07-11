package com.jpale.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "CHILDIDC")
public class ChildIdEntity {
    @Id
    private String id;

    @ManyToOne
    @JoinColumns({
        // name은 해당 테이블의 컬럼명, referencedColumnName는 부모 테이블의 컬럼명
        // name과 referencedColumnName같을 경우 referencedColumnName 생략 가능
        @JoinColumn(name = "pid1", referencedColumnName = "parent_id1"),
        @JoinColumn(name = "pid2", referencedColumnName = "parent_id2")
    })
    private ParentIdEntity parent;
}