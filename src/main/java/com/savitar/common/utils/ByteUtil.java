package com.savitar.common.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

public class ByteUtil {

    public static String toHex(byte[] buffer) {
        StringBuffer sb = getStringBuffer(buffer);
        return sb.toString();
    }

    public static ByteBuffer toByteBuffer(byte[] buffer) {
        ByteBuffer buf = ByteBuffer.allocate(buffer.length);
        buf.put(buffer);
        buf.flip();
        return buf;
    }


    private static StringBuffer getStringBuffer(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb;
    }
}
