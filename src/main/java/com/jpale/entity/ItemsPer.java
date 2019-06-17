package com.jpale.entity;

import java.io.Serializable;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TableGenerator(name="ITEMS_SEQ_GENERATOR", table="ITEMS_SEQUENCE", pkColumnValue="ITEMS_SEQ", allocationSize=1)
public abstract class ItemsPer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "ITEMS_SEQ_GENERATOR")
    @Column(name = "item_id")
    protected long id;

    @Column(name = "item_name")
    protected String name;

    @Column(name = "item_price", precision = 10, scale = 0)
    protected int price;

    @Column(name = "item_type", insertable = false, updatable = false)
    protected String itemType;

}