package com.savitar.load.png;

import java.nio.ByteBuffer;
import java.util.function.Function;

public enum IDATAFilters {
    NONE((byte) 0, data -> { // 无过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        byte b = data.get();
        matrix.add(b);
        return b & 0xFF;
    }),
    SUB((byte) 1, data -> { // SUB过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        byte b = data.get();
        int ref = matrix.getRow().getRef();
        byte b1 = ref == 0 ? 0 : matrix.getRow().getItem()[ref - 1]; // 前一个位置
        byte i = (byte) (b + b1);
        matrix.add(i);
        return i & 0xFF;
    }),
    UP((byte) 2, data -> { // UP过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        byte b = data.get();
        byte i = (byte) (b + matrix.getRow().getPrev().getItem()[matrix.getRow().getRef()]); // 上一行的此位置
        matrix.add(i);
        return i & 0xFF;
    }),
    AVG((byte) 3, data -> { // AVG过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        byte b = data.get();
        int ref = matrix.getRow().getRef();
        byte origA = ref == 0 ? 0 : matrix.getRow().getItem()[ref - 1];
        byte origB = matrix.getRow().getPrev().getItem()[ref];
        byte v = (byte) (b + Math.floor((origA + origB) / 2));
        matrix.add(v);
        return v & 0xFF;
    }),
    ROUTE((byte) 4, data -> { // ROUTE过滤器
        ByteMatrix matrix = ByteMatrix.getInstance(null);
        byte b = data.get();
        int ref = matrix.getRow().getRef();
        byte origA = ref == 0 ? 0 : matrix.getRow().getItem()[ref - 1];
        byte origB = matrix.getRow().getPrev().getItem()[ref];
        byte origC = ref == 0 ? 0 : matrix.getRow().getPrev().getItem()[ref - 1];
        matrix.add(b);
        return (byte) (b + IDATAFilters.minGradient(origA, origB, origC)) & 0xFF;
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

    private static byte minGradient(byte a, byte b, byte c) {
        int p = a + b + c;
        int pa = Math.abs(p - a);
        int pb = Math.abs(p - b);
        int pc = Math.abs(p - c);
        int pr = 0;
        if (pa <= pb && pa <= pc) {
            pr = a;
        } else if (pb <= pc) {
            pr = b;
        } else {
            pr = c;
        }
        return (byte) pr;
    }
}
