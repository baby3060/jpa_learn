package com.jpale.common;

public enum BoardType {
    
    NOTIFY(0), USER(2);

    private int value;

    BoardType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BoardType valueOf(int value) {
        switch(value) {
            case 0 : return NOTIFY;
            case 2 : return USER;
            default : throw new AssertionError("잘못된 게시판 타입입니다.");
        }
    }
}