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
public class BoardIdentity {
    @Id
    @Column(name="board_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long no;

    private String title;

    @OneToOne(mappedBy = "board")
    private BoardDetail boardDetail;
}