package com.jpale.entity;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("T")
public class Taxi extends Transportation {

    @Column(name = "car_number", length = 10)
    private String carNumber;

    @Column(name = "base_price")
    private int basePrice;
}