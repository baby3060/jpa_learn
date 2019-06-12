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
@EqualsAndHashCode(exclude = {"orderDate", "orderStatus", "orderMember"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Orders")
public class Order {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @PrePersist
    protected void orderDateInit() {
        orderDate = new Date();

        if( this.orderStatus == null ) {
            this.orderStatus = OrderStatus.ORDERING;
        }
    }

    @Convert(converter = OrderStatusConverter.class)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    // member_id라는 컬럼 생성
    @ManyToOne
    @JoinColumn(name = "member_id", nullable=false)
    private Member orderMember;

    public void setOrderMember(Member member) {
        if( this.orderMember != null ) {
            this.orderMember.getOrderList().remove(this);
        }

        this.orderMember = member;
        this.orderMember.getOrderList().add(this);
    }

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItemList;

    {
        orderItemList = new ArrayList<OrderItem>();
    }
}