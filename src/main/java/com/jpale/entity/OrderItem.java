package com.jpale.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"orderId", "itemId", "orderCount", "orderPrice"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ORDERITEM")
public class OrderItem {
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue
    private long id;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "item_id")
    private long itemId;

    @Column(name = "order_count")
    private int orderCount;

    @Column(name = "order_price")
    private long orderPrice;

}