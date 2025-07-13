package com.liteGrass.learnNetty.baseNetty.baseNIO;


import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName: StrBufferLearn
 * @Author: liteGrass
 * @Date: 2025/7/1 0:15
 * @Description: 字符串操作学习
 */
public class StrBufferLearn {

    /**
    * @Auther: liteGrass
    * @Date: 2025/7/1 0:15
    * @Desc: 字符串操作
    */
    @Test
    void testMethodStrBuffer() {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("张健草");
        // 此时buffer自动为转化为读模式，不能多次调用flip进行转换
        System.out.println(buffer.position()); // 0
        System.out.println(buffer.limit()); //9
        System.out.println(buffer.capacity());  //15

        CharBuffer decode = StandardCharsets.UTF_8.decode(buffer);
        System.out.println(buffer.position()); // 9
        System.out.println(buffer.limit()); //9
        System.out.println(buffer.capacity());  //15

        System.out.println(decode.toString());

    }

}
