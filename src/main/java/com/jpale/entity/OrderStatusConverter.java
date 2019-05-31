package com.jpale.entity;

import javax.persistence.*;

import com.jpale.common.OrderStatus;

public class OrderStatusConverter implements AttributeConverter<OrderStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OrderStatus orderStatus) {
        return orderStatus.getStatusCode();
    }

    @Override
    public OrderStatus convertToEntityAttribute(Integer orderStatus) {
        return OrderStatus.valueOf(orderStatus);
    }
}