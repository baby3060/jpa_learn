package com.jpale.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BoardDetail {
    @Id
    private long boardNo;

    // 자체 매핑
    @MapsId
    @OneToOne
    @JoinColumn(name = "board_no")
    private BoardIdentity board;

    private String content;
}