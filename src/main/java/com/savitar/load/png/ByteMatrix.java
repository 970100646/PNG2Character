package com.savitar.load.png;

import lombok.Data;

/*
 * @author Savitar
 * @date 2021/8/10 16:47
 */
@Data
public class ByteMatrix {
    private Line row;
    private int width;
    private int size;

    private static volatile ByteMatrix matrix;

    private ByteMatrix(Integer width) {
        this.width = width;
        this.size = 0;
    }

    public static ByteMatrix getInstance(Integer width) {
        if (matrix == null) {
            synchronized (ByteMatrix.class) {
                if (matrix == null) {
                    matrix = new ByteMatrix(width);
                }
            }
        }
        return matrix;
    }

    @Data
    class Line {
        private Line prev;
        private byte[] item;
        private Line next;
        private int ref;

        public Line(Line prev, byte[] datas, Line next) {
            this.prev = prev;
            this.item = datas;
            this.next = next;
            this.ref = 0;
        }
    }

    public void add(byte data) {
        try {
            this.row.item[this.row.ref++] = data;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void newRow() {
        if (this.size == 0) {
            this.row = new Line(null, newByteArr(), null);
            return;
        }
        Line t = this.row;
        this.row = new Line(t, newByteArr(), null);
        t.next = this.row;
    }

    private byte[] newByteArr() {
        this.size++;
        return new byte[this.width];
    }


}
