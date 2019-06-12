package com.jpale.entity;

import javax.persistence.*;

import lombok.*;

import com.jpale.common.Category;

import java.util.*;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ICATEGORY")
public class ItemCategory {
    @Id
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    @Convert(converter = CategoryConverter.class)
    private Category category;

    @OneToMany(mappedBy = "category")
    private List<Item> itemList;

    {
        itemList = new ArrayList<Item>();
    }
}