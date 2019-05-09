package com.jpale.entity;

import lombok.*;

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
    private Integer userAge;

    @NonNull
    // db 컬럼명과 동일하므로, 생략
    private String password;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}