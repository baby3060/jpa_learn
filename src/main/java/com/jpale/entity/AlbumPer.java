package com.jpale.entity;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AlbumPer extends ItemsPer {
    private String artist;
}