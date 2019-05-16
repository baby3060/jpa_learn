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
@Entity
@Table(name="BOARD")
public class Board {

    @Id
    @Column(name="board_no")
    @NonNull
    private long boardNo;

    @Column(name="writer_id")
    @NonNull
    private String writerId;

    @Column(name="write_date")
    private Date writeDate;

    @Column(name="last_modified_date")
    private Date lastModifiedDate;

    private String description;

    @Convert(converter = BoardTypeConverter.class)
    @Column(name="board_type")
    private BoardType boardType;
}