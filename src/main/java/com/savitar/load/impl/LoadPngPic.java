package com.savitar.load.impl;

import com.savitar.common.exception.FileFormatErrorException;
import com.savitar.common.utils.ByteUtil;
import com.savitar.common.utils.ZLibUtils;
import com.savitar.load.LoadPic;
import com.savitar.load.Picture;
import com.savitar.load.png.ByteMatrix;
import com.savitar.load.png.IDATAFilters;
import com.savitar.load.png.PNGColorType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.function.Function;

/*
 * @author Savitar
 * @date 2021/8/10 16:47
 */
@Slf4j
public class LoadPngPic extends Picture implements LoadPic {

    private static final long PNG_HEAD = 0x89504E470D0A1A0AL;
    private static final int PLTE = 0x504c5445;
    private static final int IDAT = 0x49444154;
    private static final int IEND = 0x49454e44;

    private static ByteMatrix matrix = null;

    public LoadPngPic(String path) {
        super(path);
    }

    @Override
    public Picture load() throws IOException {
        Picture picture = doLoad();
        picture.getBuffer().mark();
        if (!validate(picture.getBuffer()::getLong, PNG_HEAD)) {
            throw new FileFormatErrorException("File format is must be PNG!");
        }
        read(picture);
        return picture;
    }

    private void read(Picture picture) {
        readHead(picture);
        readIDAT(picture);
    }

    /**
     * 读取IDAT数据块
     *
     * @param picture
     */
    private void readIDAT(Picture picture) {
        ByteBuffer buffer = picture.getBuffer();
        int length = buffer.getInt();
        int identifi = buffer.getInt();
        if (identifi != IDAT) {
            buffer.get(new byte[length + 4]);
            readIDAT(picture);
            return;
        }
        byte[] data = new byte[length];
        buffer.get(data);
        byte[] decompress = ZLibUtils.decompress(data);
        byte colorType = picture.getColorType();
        if (PNGColorType.TRUE_COLOR == colorType) { // 真彩图像
            this.matrix = ByteMatrix.getInstance(picture.getWidth() * 3);
            print(this::trueColorPrint, ByteUtil.toByteBuffer(decompress), picture);
        }
        if (PNGColorType.A_CHANNEL_TRUE_COLOR == colorType) { // 带α通道数据的真彩色图像
            this.matrix = ByteMatrix.getInstance(picture.getWidth() * 4);
            print(this::aTrueColorPrint, ByteUtil.toByteBuffer(decompress), picture);
        }
    }

    //带α通道数据的真彩色图像打印
    private void aTrueColorPrint(ByteBuffer data, Picture picture) {
        System.out.println();
        if (!data.hasRemaining()) {
            return;
        }
        int filterType = data.get() & 0xFF;
        Function<ByteBuffer, Integer> read = IDATAFilters.getRead(filterType);
        matrix.newRow();  //
        for (int i = 0; i < picture.getWidth(); i++) {
            int r = read.apply(data);
            int g = read.apply(data);
            int b = read.apply(data);
            int a = read.apply(data);
            System.out.print(getChar(r, g, b, a));
        }
        aTrueColorPrint(data, picture);
    }

    //真彩图像打印
    private void trueColorPrint(ByteBuffer data, Picture picture) {
        System.out.println();
        if (!data.hasRemaining()) {
            return;
        }
        int filterType = data.get() & 0xFF;
        Function<ByteBuffer, Integer> read = IDATAFilters.getRead(filterType);
        matrix.newRow();  //
        for (int i = 0; i < picture.getWidth(); i++) {
            int r = read.apply(data);
            int g = read.apply(data);
            int b = read.apply(data);
            System.out.print(getChar(r, g, b, 256));
        }
        trueColorPrint(data, picture);
    }

    private void readHead(Picture picture) {
        ByteBuffer buffer = picture.getBuffer();
        buffer.getLong(); // 偏移8个字节
        picture.setWidth(buffer.getInt()); // 宽度
        picture.setHeight(buffer.getInt()); // 高度
        picture.setDepth(buffer.get()); // 图像深度
        picture.setColorType(buffer.get()); // 颜色类型
        buffer.get(); //压缩方法
        picture.setFilterMethod(buffer.get()); //过滤器方法
        picture.setInterlaceMethod(buffer.get()); //隔行扫描方法
        buffer.getInt();
    }

}
