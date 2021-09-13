package com.savitar.app;

import com.savitar.load.Picture;
import com.savitar.load.impl.LoadPngPic;
import com.savitar.load.png.ByteMatrix;
import sun.nio.ch.FileChannelImpl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SavitarApplication {
    public static void main(String[] args) {
        try {
            String path = "D:/temp/logo.png";
            LoadPngPic loadPngPic = new LoadPngPic(path);
            Picture picture = loadPngPic.load();
            System.out.println(picture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
