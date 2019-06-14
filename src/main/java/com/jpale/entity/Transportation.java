package com.jpale.entity;

import lombok.*;
import javax.persistence.*;

import com.jpale.common.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = {"areaCode", "driver"})
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TRTYPE")
public abstract class Transportation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trans_id")
    protected long transId;

    @Column(name = "area_code")
    @Convert(converter = AreaCodeConverter.class)
    protected AreaCode areaCode;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    protected TransDriver driver;

    public void setDriver(TransDriver driver) {
        // 초기화
        if( this.driver != null ) {
            this.driver.getTransList().remove(this);
        }

        // 할당
        this.driver = driver;
        // List 할당
        if( this.driver != null ) {
            this.driver.getTransList().add(this);
        }
    }
}