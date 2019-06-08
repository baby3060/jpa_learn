package com.jpale.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"orderCount", "orderPrice"})
@AllArgsConstructor
@Entity
@Table(name="ORDERITEM")
@IdClass(OrderItemKey.class)
public class OrderItem {
    
    public OrderItem() {}

    public OrderItem(Order order, Item item) {
        if( this.order != null ) {
            this.order.getOrderItemList().remove(this);
        }

        this.order = order;
        this.order.getOrderItemList().add(this);

        this.item = item;
    }

    @Id
    @NonNull
    @ManyToOne
    @JoinColumn(
        name = "order_id",
        referencedColumnName = "order_id"
    )
    private Order order;
    public void setOrder(Order order) {
        if( this.order != null ) {
            this.order.getOrderItemList().remove(this);
        }

        this.order = order;
        this.order.getOrderItemList().add(this);
    }

    @Id
    @NonNull
    @OneToOne
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