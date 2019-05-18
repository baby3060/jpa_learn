package com.jpale.entity;

import lombok.*;

import javax.persistence.*;

import java.util.Date;
import com.jpale.entity.BoardTypeConverter;
import com.jpale.common.BoardType;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"writerId", "writeDate", "lastModifiedDate", "description", "boardType"})
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
// ORACLE 확장 대비
/*
@SequenceGenerator(
    name="BOARD_SEQUENCE_GENERATOR",
    sequenceName = "BOARD_SEQ",
    initialValue = 1,
    allocationSize = 1
)
*/
@Entity
@Table(name="BOARD")
public class Board {
    @Id
    @Column(name="board_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private long boardNo;

    @NonNull
    @JoinColumn(name = "writer_id", referencedColumnName = "id")
    // Member 한 명 당 게시글 여러 개
    @ManyToOne
    private Member member;
    
    @Column(name="write_date")
    private Date writeDate;

    // persist 호출 전 해당 값으로 저장
    @PrePersist
    protected void onWrite() {
        writeDate = new Date();
        lastModifiedDate = new Date();
    }

    @Column(name="last_modified_date")
    private Date lastModifiedDate;

    // update 하기 전에 해당 값으로 저장
    @PreUpdate
    protected void boardUpdate() {
        lastModifiedDate = new Date();
    }

    private String description;

    @Convert(converter = BoardTypeConverter.class)
    @Column(name="board_type")
    private BoardType boardType;
}