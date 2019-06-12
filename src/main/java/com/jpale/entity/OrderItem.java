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
// @IdClass(OrderItemKey.class)
public class OrderItem {
    
    public OrderItem() {}

    public OrderItem(Order order, Item item) {
        // Order 정리
        if( this.order != null ) {
            this.order.getOrderItemList().remove(this);
        }

        this.order = order;

        if( order != null ) {
            this.order.getOrderItemList().add(this);
        }

        // Order 정리
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_item_id;
    
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

        if( order != null ) {
            this.order.getOrderItemList().add(this);
        }
    }

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