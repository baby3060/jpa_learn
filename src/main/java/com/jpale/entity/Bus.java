package com.jpale.entity;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("B")
public class Bus extends Transportation {
    @Column(name = "bus_number")
    private int busNumber;
    
    @Column(name = "adult_price")
    private int adultPrice;
    @Column(name = "child_price")
    private int childPrice;
    @Column(name = "student_price")
    private int studentPrice;
    @Column(name = "time_alloc")
    private int timeAlloc;

    @Column(name = "car_number", length = 10)
    private String carNumber;
}