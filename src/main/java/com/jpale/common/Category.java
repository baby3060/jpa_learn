package com.jpale.common;

public enum Category {

    ALBUM("ALB");

    private String category;

    Category(String category) {
        this.category = category;
    }

    public String toString() {
        return "[" + this.category + "]";
    }

    public String getCategory() {
        return this.category;
    }
}