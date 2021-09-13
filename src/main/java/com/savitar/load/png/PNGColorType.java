package com.savitar.load.png;

/**
 * 颜色类型：
 * 0：灰度图像, 1，2，4，8或16
 * 2：真彩色图像，8或16
 * 3：索引彩色图像，1，2，4或8
 * 4：带α通道数据的灰度图像，8或16
 * 6：带α通道数据的真彩色图像，8或16
 */
public class PNGColorType {
    public static final byte GRAYSCALE = 0; // 灰度图像
    public static final byte TRUE_COLOR = 2; //真彩图像
    public static final byte INDEX_COLOR = 3; //索引彩色图像
    public static final byte A_CHANNEL_GRAYSCALE = 4; //带α通道数据的灰度图像
    public static final byte A_CHANNEL_TRUE_COLOR = 6; //带α通道数据的真彩色图像
}
