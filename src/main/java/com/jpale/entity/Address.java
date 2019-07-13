package com.jpale.entity;

import javax.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @Column(length = 60)
    private String city;
    @Column(length = 60)
    private String street;
    @Column(length = 60)
    private String zipcode;

}