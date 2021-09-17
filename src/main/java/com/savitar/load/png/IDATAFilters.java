package com.savitar.load.png;

import java.nio.ByteBuffer;
import java.util.function.Function;

public enum IDATAFilters {
    NONE((byte) 0, data -> { // 无过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        int b = data.get() & 0xFF;
        matrix.add((byte) b);
        return b & 0xFF;
    }),
    SUB((byte) 1, data -> { // SUB过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        int raw = data.get() & 0xFF;
        int ref = matrix.getRow().getRef();
        int bpp = matrix.getBpp();
        int b1 = ref < bpp ? 0 : matrix.getRow().getItem()[ref - bpp] & 0xFF; // 前一个位置
        byte i = (byte) (raw + b1);
        matrix.add(i);
        return i & 0xFF;
    }),
    UP((byte) 2, data -> { // UP过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        int raw = data.get() & 0xFF;
        int prior = matrix.getRow().getPrev().getItem()[matrix.getRow().getRef()] & 0xFF;
        byte r = (byte) (raw + prior); // 上一行的此位置
        matrix.add(r);
        return r & 0xFF;
    }),
    AVG((byte) 3, data -> { // AVG过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        int raw = data.get() & 0xFF;
        int ref = matrix.getRow().getRef();
        int bpp = matrix.getBpp();
        int origA = ref < bpp ? 0 : matrix.getRow().getItem()[ref - bpp] & 0xFF;
        int origB = matrix.getRow().getPrev().getItem()[ref] & 0xFF;
        byte v = (byte) (raw + (origA + origB) / 2);
        matrix.add(v);
        return v & 0xFF;
    }),
    ROUTE((byte) 4, data -> { // ROUTE过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        int raw = data.get() & 0xFF;
        int ref = matrix.getRow().getRef();
        int bpp = matrix.getBpp();
        int origA = ref < bpp ? 0 : matrix.getRow().getItem()[ref - bpp] & 0xFF;
        int origB = matrix.getRow().getPrev().getItem()[ref] & 0xFF;
        int origC = ref < bpp ? 0 : matrix.getRow().getPrev().getItem()[ref - bpp] & 0xFF;
        byte r = (byte) (raw + IDATAFilters.minGradient(origA, origB, origC));
        matrix.add(r);
        return r & 0xFF;
    });

    private byte code;
    private Function<ByteBuffer, Integer> read;

    IDATAFilters(byte code, Function<ByteBuffer, Integer> read) {
        this.code = code;
        this.read = read;
    }

    public static Function<ByteBuffer, Integer> getRead(int filterType) {
        for (IDATAFilters value : IDATAFilters.values()) {
            if (filterType == value.code) {
                return value.read;
            }
        }
        return null;
    }

    private static int minGradient(int a, int b, int c) {
        int p = a + b - c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        int pr ;
        if ((pa <= pb) && (pa <= pc)) {
            pr = a;
        } else if (pb <= pc) {
            pr = b;
        } else {
            pr = c;
        }
        return pr;
    }
}
