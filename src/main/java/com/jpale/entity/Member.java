package com.jpale.entity;

import lombok.*;
import java.util.List;
import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"userName", "userAge", "password"})
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="MEMBER")
public class Member {
    @Id
    @Column(name="ID")
    @NonNull
    private String userId;

    @Column(name="name")
    @NonNull
    private String userName;

    @Column(name="age")
    @NonNull
    private Integer userAge;

    @NonNull
    // db 컬럼명과 동일하므로, 생략
    private String password;

    // 참조키가 Board에 존재하므로 연관관계의 주인이 아니다. 따라서 mappedBy 속성을 사용함
    @OneToMany(mappedBy = "member")
    private List<Board> boardList;
}