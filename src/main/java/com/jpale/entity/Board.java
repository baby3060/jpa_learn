package com.jpale.entity;

import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;

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
    @NonNull
    @Column(name="board_no", precision = 10, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardNo;

    @NonNull
    @JoinColumn(name = "writer_id", referencedColumnName = "id", nullable = false)
    // Member 한 명 당 게시글 여러 개
    @ManyToOne
    private Member member;
    
    // 양 쪽 관계 모두 유지하기 위해서 필요
    // 작성자는 변하지 않으므로, 그대로 유지
    public void setMember(Member member) {
        this.member = member;

        if( this.member != null ) {
            
            if( this.member.getBoardList() == null ) {
                this.member.setBoardList(new ArrayList<Board>());
            }
        }
        
        member.getBoardList().add(this);
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="write_date")
    private Date writeDate;

    // persist 호출 전 해당 값으로 저장
    @PrePersist
    protected void onWrite() {
        writeDate = new Date();
        lastModifiedDate = new Date();
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="last_modified_date")
    private Date lastModifiedDate;

    // update 하기 전에 해당 값으로 저장
    @PreUpdate
    protected void boardUpdate() {
        lastModifiedDate = new Date();
    }

    @Column(name="description", length=1000)
    private String description;

    @Convert(converter = BoardTypeConverter.class)
    @Column(name="board_type", precision=1, scale = 0, nullable = false)
    private BoardType boardType;
}