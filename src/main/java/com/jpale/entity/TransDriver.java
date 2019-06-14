package com.jpale.entity;

import javax.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TransDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dirver_id")
    private long driverId;

    @Column(name = "driver_name")
    private String driverName;
    
    @OneToMany(mappedBy = "driver")
    private List<Transportation> transList;

    {
        transList = new ArrayList<Transportation>();
    }
}