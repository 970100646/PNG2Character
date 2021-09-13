package com.savitar.load;

import lombok.Data;
import lombok.ToString;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/*
 * @author Savitar
 * @date 2021/8/10 16:47
 */
@Data
@ToString
public abstract class Picture {

    private int width;
    private int height;
    private byte depth;
    private byte colorType;
    private byte filterMethod; //过滤器方法
    private byte interlaceMethod; //各行扫描方法
    private ByteBuffer buffer;

    private final String asciiChar = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/|()1{}[]?-_+~<>i!lI;:,^`'.";

    private Path path;

    public Picture(String path) {
        this.path = Paths.get(path);
    }

    protected Picture doLoad() {
        try (FileInputStream stream = new FileInputStream(path.toFile())) {
            FileChannel channel = stream.getChannel();
            this.buffer = ByteBuffer.allocate((int) channel.size());
            channel.read(this.buffer);
            this.buffer.flip();
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected boolean validate(Supplier<Long> supplier, Long head) {
        return head.equals(supplier.get());
    }


    protected void print(DoubleParamConsumer<ByteBuffer, Picture> consumer, ByteBuffer data, Picture picture) {
        consumer.accept(data, picture);
    }

    protected char getChar(int r, int g, int b, int a) {
        if (a == 0) {
            return ' ';
        }
        int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
        char c = this.asciiChar.charAt((int) (gray / (256.0D % this.asciiChar.length())));
        return c;
    }

}
