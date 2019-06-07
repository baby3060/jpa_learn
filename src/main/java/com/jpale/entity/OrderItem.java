package com.jpale.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"orderCount", "orderPrice"})
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="ORDERITEM")
@IdClass(OrderItemKey.class)
public class OrderItem {
    
    @Id
    @NonNull
    @ManyToOne
    @JoinColumn(
        name = "order_id",
        referencedColumnName = "order_id"
    )
    private Order order;

    @Id
    @NonNull
    @ManyToOne
    @JoinColumn(
        name = "item_id",
        referencedColumnName = "item_id"
    )
    private Item item;

    @Column(name = "order_count")
    private int orderCount;

    @Column(name = "order_price")
    private long orderPrice;
    
}