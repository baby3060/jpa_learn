package com.jpale.entity;

import javax.persistence.*;

import com.jpale.common.Category;

public class CategoryConverter implements AttributeConverter<Category, String> {
    @Override
    public String convertToDatabaseColumn(Category category) {
        return category.getCategory();
    }

    @Override
    public Category convertToEntityAttribute(String category) {
        return Category.valueOf(category);
    }
}