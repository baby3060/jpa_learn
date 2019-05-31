package com.jpale.common;

public enum OrderStatus {
    
    ORDERING(0, "준비중"), SHIPPING(1, "배송중"), COMPLETE(2, "배송완료"), CANCLE(9, "취소");

    private int statusCode;
    private String statusValue;

    OrderStatus(int statusCode, String statusValue) {
        this.statusCode = statusCode;
        this.statusValue = statusValue;
    }

    public static OrderStatus valueOf(int statusCode) {
        switch(statusCode) {
            case 0 : return ORDERING;
            case 1 : return SHIPPING;
            case 2 : return COMPLETE;
            case 9 : return CANCLE;

            default : throw new AssertionError("잘못된 상태값");
        }
    }

    @Override
    public String toString() {
        return "[" + this.statusCode + "]" + this.statusValue;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusValue() {
        return this.statusValue;
    }

}