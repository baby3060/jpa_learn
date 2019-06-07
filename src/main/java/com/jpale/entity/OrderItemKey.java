package com.jpale.entity;

import lombok.*;

import java.io.Serializable;

import javax.persistence.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private Order order;
    private Item item;
}