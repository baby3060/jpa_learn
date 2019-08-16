package com.jpale.entity;

import java.util.Date;

import lombok.*;
import java.util.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"itemName", "price", "stockQt", "registDate"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ITEM")
public class Item {

    @Id
    @Column(name = "item_id", precision = 12, scale=0)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

    @Column(name = "item_name", length = 100)
    private String itemName;

    @Column(precision = 12)
    private int price;

    @Column(name = "stock_qt", precision = 10)
    private int stockQt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "regist_date")
    private Date registDate;

    @PrePersist
    public void dateFill() {
        registDate = new Date();
    }

    @ManyToOne
    @JoinColumn(name = "category")
    private ItemCategory category;

    public void setCategory(ItemCategory category) {
        if( this.category != null ) {
            this.category.getItemList().remove(this);
        }

        this.category = category;

        if( category != null ) {
            this.category.getItemList().add(this);
        }
    }

    @OneToMany(mappedBy = "item")
    private List<OrderItem> orderItemList;

    {
        orderItemList = new ArrayList<OrderItem>();
    }

}