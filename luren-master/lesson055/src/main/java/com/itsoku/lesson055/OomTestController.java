package com.itsoku.lesson055;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/2 19:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class OomTestController {

    public static List<byte[]> oomList = new ArrayList<>();

    @GetMapping("/oomTest")
    public String oomTest() {
        while (true) {
            //不断向列表中添加大对象, 10MB
            oomList.add(new byte[1024 * 1024 * 10]);
        }
    }
}