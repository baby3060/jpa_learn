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
@AttributeOverride(name = "id", column = @Column(name = "seller_id"))
public class Seller extends BaseEntity {
    @Column(name = "shop_name")
    private String shopName;
}