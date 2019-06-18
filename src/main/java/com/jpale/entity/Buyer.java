package com.jpale.entity;

import lombok.*;
import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "buyer_id")),
    @AttributeOverride(name = "name", column = @Column(name = "buyer_name"))
})
public class Buyer extends BaseEntity {
    private String email;

    private String address;
}