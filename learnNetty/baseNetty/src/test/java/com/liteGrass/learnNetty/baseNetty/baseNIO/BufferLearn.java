package com.liteGrass.learnNetty.baseNetty.baseNIO;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @ClassName: BufferLearn
 * @Author: liteGrass
 * @Date: 2025/6/30 22:49
 * @Description: 学习相关buffer内容
 */
public class BufferLearn {
    /**
    * @Auther: liteGrass
    * @Date: 2025/6/30 22:49
    * @Desc: buffer相关内容学习
    */
    @Test
    void testMethod1() {
        try (FileChannel channel = FileChannel.open(Paths.get("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt"), StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            System.out.println(buffer.position()); // 0
            System.out.println(buffer.limit()); // 10
            System.out.println(buffer.capacity());  //10

            //开始进行写入
            System.out.println("-------------------------------------------------");
            buffer.put(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g'});
            System.out.println(buffer.position()); // 7
            System.out.println(buffer.limit()); // 10
            System.out.println(buffer.capacity());  //10

            // 切换到写模式
            System.out.println("-------------------------------------------------");
            buffer.flip();
            System.out.println(buffer.position()); // 0
            System.out.println(buffer.limit()); // 7
            System.out.println(buffer.capacity());  //10

            System.out.println("-------------------------------------------------");
            buffer.get();
            buffer.get();
            System.out.println(buffer.position()); // 2
            System.out.println(buffer.limit()); // 7
            System.out.println(buffer.capacity());  //10

            // 切换到写模式
            System.out.println("-------------------------------------------------");
            buffer.compact();
            System.out.println(buffer.position()); // 5
            System.out.println(buffer.limit()); //10
            System.out.println(buffer.capacity());  //10


            System.out.println("-------------------------------------------------");
            buffer.flip();

            buffer.rewind(); // 就是把p指针重新置为0
            System.out.println(buffer.position()); // 0
            System.out.println(buffer.limit()); // 5
            System.out.println(buffer.capacity());  //10


            System.out.println("-------------------------------------------------");
            buffer.get();
            buffer.get();
            buffer.mark(); //标志到2得位置
            buffer.reset();
            System.out.println(buffer.position()); // 2
            System.out.println(buffer.limit()); // 5
            System.out.println(buffer.capacity());  //10

        } catch (IOException e) {

        }

    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/1 16:20
    * @Desc: 粘包半包问题
    */
    @Test
    void testMethod2() {

        ByteBuffer buffer = ByteBuffer.allocate(50);
        buffer.put("HI ZhangJianCao\nI love you.\n Do you".getBytes());
        doLineSplit(buffer);
    }

    /**
     * 进行拆包
     * @param buffer
     */
    void doLineSplit(ByteBuffer buffer) {
        buffer.flip();
        // 1、先找到/n
        for (int i = 0; i < buffer.limit(); i++) {
            if (buffer.get(i) == '\n') {
                // 2、开始拷贝数据到/n
                // 3、定义一个可以读取长度的buffer
                int length = i + 1  - buffer.position();
                ByteBuffer destBuffer = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    destBuffer.put(buffer.get());
                }
                // 获取到相关文件
                destBuffer.flip();
                System.out.println(StandardCharsets.UTF_8.decode(destBuffer));
            }
        }
        // 3、然后进行移动数据, 本次查找完成之后，进行移动
        buffer.compact();
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/1 16:38
    * @Desc: 文件拷贝工作
    */
    @Test
    void testMethod3() {
        // 进行文件拷贝工作
        try(FileChannel originalFile = FileChannel.open(Paths.get("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt"), StandardOpenOption.READ);
            FileChannel targetFile = FileChannel.open(Paths.get("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test2.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ) {
            long begin = 0;
            // 开始进行相关文件传递工作
            while ( begin < originalFile.size()) {
                begin += originalFile.transferTo(begin, originalFile.size(), targetFile);
            }
            // 他的限制每次只能传输小于2G

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}



















