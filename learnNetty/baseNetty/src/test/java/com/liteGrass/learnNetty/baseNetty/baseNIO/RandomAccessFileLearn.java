package com.liteGrass.learnNetty.baseNetty.baseNIO;


import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: RandomAccessFileLearn
 * @Author: liteGrass
 * @Date: 2025/6/30 7:29
 * @Description: 指针文件学习
 */
public class RandomAccessFileLearn {

    /**
    * @Auther: liteGrass
    * @Date: 2025/6/30 7:30
    * @Desc: 方法学习
    */
    @Test
    void testMethod1() throws FileNotFoundException {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile("D:\\development\\codeRepo\\G-JDAT\\learnNetty\\baseNetty\\src\\main\\resources\\test.txt", "rw");) {
            // 打印指针位置
            System.out.println(randomAccessFile.getFilePointer());   // 0
            // 如果现在开始写入的话就是从头开始写入

            // 文件指针位置定位到指定位置，定位到文件的末尾位置
            randomAccessFile.seek(randomAccessFile.length());
            System.out.println(randomAccessFile.getFilePointer());   // 13


            // 从末尾位置开始进行写
            randomAccessFile.write(StrUtil.bytes("你好,张健草1\n你好张健草2", StandardCharsets.UTF_8));
            System.out.println(randomAccessFile.getFilePointer());   // 现在到达文件末尾位置

            // 开始读取文件
            // 1、需要先让文件指定到指定位置
            randomAccessFile.seek(0);
            randomAccessFile.read(new byte[10], 2, 7);
            // 读取文件的是否文件指针也会向后移动
            System.out.println(randomAccessFile.getFilePointer());


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
