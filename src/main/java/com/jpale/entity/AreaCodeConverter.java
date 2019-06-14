package com.jpale.entity;

import javax.persistence.AttributeConverter;

import com.jpale.common.AreaCode;

public class AreaCodeConverter implements AttributeConverter<AreaCode, Integer> {
    @Override
    public Integer convertToDatabaseColumn(AreaCode areaCode) {
        if( areaCode == null ) {
            return AreaCode.SEOUL.getACode();
        } else {
            return areaCode.getACode();
        }
    }

    @Override
    public AreaCode convertToEntityAttribute(Integer aCode) {
        
        return AreaCode.valueOf(aCode);
        
    }
}