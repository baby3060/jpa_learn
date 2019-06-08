package com.jpale.entity;

import com.jpale.common.*;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.*;

import java.util.Date;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"orderDate", "status", "orderMember"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="ORDERS")
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private long orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @PrePersist
    protected void orderDateInit() {
        orderDate = new Date();

        if( this.status == null ) {
            this.status = OrderStatus.ORDERING;
        }
    }

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    // member_id라는 컬럼 생성
    @ManyToOne
    @JoinColumn(name = "member_id", nullable=false)
    private Member orderMember;

    public void setOrderMember(Member member) {
        if( this.orderMember != null ) {
            this.orderMember.getOrderList().remove(this);
        }

        this.orderMember = orderMember;
        this.orderMember.getOrderList().add(this);
    }

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    {
        orderItemList = new ArrayList<OrderItem>();
    }
}