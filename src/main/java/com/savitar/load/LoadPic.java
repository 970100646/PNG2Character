package com.savitar.load;


import java.io.IOException;
import java.nio.ByteBuffer;

/*
 * @author Savitar
 * @date 2021/8/10 16:47
 */
public interface LoadPic {

    Picture load() throws IOException;
}
