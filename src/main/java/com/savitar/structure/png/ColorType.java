package com.savitar.structure.png;

public enum ColorType {
    ;

    ColorType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private byte code;
    private String desc;
}
