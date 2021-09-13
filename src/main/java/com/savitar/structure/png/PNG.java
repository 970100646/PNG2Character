package com.savitar.structure.png;

public enum PNG {
    IHDR("文件头数据块",13),
    ;

    PNG(String desc, int length) {
        this.desc = desc;
        this.length = length;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    private String desc;
    private int length;
}
