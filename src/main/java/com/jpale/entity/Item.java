package com.jpale.entity;

import java.util.Date;

import lombok.*;

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
}