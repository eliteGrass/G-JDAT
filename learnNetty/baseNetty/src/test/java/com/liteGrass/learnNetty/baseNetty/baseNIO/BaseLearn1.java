package com.liteGrass.learnNetty.baseNetty.baseNIO;


import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @ClassName BaseLearn1
 * @Company Huahui Information Technology Co., LTD.
 * @Author elitegrass
 * @Date 2025/6/17 00:05
 * @Description
 */
public class BaseLearn1 {

    @Test
    public void testMethod1() throws IOException {
        // 获取channel
        FileChannel channel = new FileInputStream("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt").getChannel();
        // 获取buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        channel.read(buffer);
        // 切换为读模式
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.print((char) buffer.get());
        }
        buffer.clear();
    }

    @Test
    public void testMethod2() throws IOException {
        // 获取channel
        FileChannel channel = new FileInputStream("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(10);
        while (true) {
            // 获取buffer
            if (channel.read(buffer) == -1) {
                break;
            }
            // 切换为读模式
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }
            buffer.clear();
        }
    }


    @Test
    public void testMethod3() throws FileNotFoundException {
        try (FileChannel channel = new RandomAccessFile("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt", "rw").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                // 获取buffer
                if (channel.read(buffer) == -1) {
                    break;
                }
                // 切换为读模式
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    public void testMethod4() throws IOException {
        // 不用进行相关的关闭操作
        try (FileChannel channel = FileChannel.open(Paths.get("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt"), StandardOpenOption.READ)) {
            ByteBuffer allocate = ByteBuffer.allocate(10);
            while (true) {
                // 开始进行相关的写操作
                if (channel.read(allocate) == -1) {
                    break;
                }
                // 进行读操作
                allocate.flip();
                while (allocate.hasRemaining()) {
                    System.out.println("allocate.get() = " + (char) allocate.get());
                }
                allocate.clear();
            }

        } catch (IOException e) {

        }

    }

}
