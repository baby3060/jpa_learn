package com.jpale.entity;

import javax.persistence.*;

import com.jpale.common.BoardType;

@Converter
public class BoardTypeConverter implements AttributeConverter<BoardType, Integer>  {
    @Override
    public Integer convertToDatabaseColumn(BoardType boardType) {
        return boardType.getValue();
    }

    @Override
    public BoardType convertToEntityAttribute(Integer boardType) {
        return BoardType.valueOf(boardType);
    }
}