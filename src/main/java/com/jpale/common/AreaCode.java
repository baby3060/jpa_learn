package com.jpale.common;

public enum AreaCode {

    SEOUL(0, "SEOUL"), BUSAN(1, "BUSAN"), DAEGU(2, "DAEGU");

    private int aCode;
    private String localName;

    AreaCode(int aCode, String localName) {
        this.aCode = aCode;
        this.localName = localName;
    }

    public static AreaCode valueOf(int aCode) {
        switch(aCode) {
            case 0 : return SEOUL;
            case 1 : return BUSAN;
            case 2 : return DAEGU;
            default : throw new AssertionError("잘못된 지역 코드입니다.");
        }
    }

    public int getACode() {
        return this.aCode;
    }

    public String toString() {
        return "[" + this.aCode + "] " + this.localName;
    }

}