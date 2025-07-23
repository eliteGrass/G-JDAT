package com.itsoku.lesson045;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/6/5 20:36 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class IndexController {
    //get请求
    @RequestMapping("/get")
    public String get() {
        return "get";
    }

    //post请求，模拟表单提交
    @PostMapping("/post")
    public Map<String, String[]> post(HttpServletRequest request) {
        return request.getParameterMap();
    }

    //post请求json数据
    @PostMapping("/body")
    public List<Integer> body(@RequestBody List<Integer> list) {
        return list;
    }

    //put请求
    @PutMapping("/put")
    public String put() {
        return "put";
    }

    //模拟多文件上传，顺便带上表单数据
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file1") MultipartFile file1,
                                      @RequestParam("file2") MultipartFile file2,
                                      User user,
                                      HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<>();
        result.put("file1.size", file1.getSize());
        result.put("file1.name", file1.getName());

        result.put("file2.size", file2.getSize());
        result.put("file2.name", file2.getName());
        result.put("file2.originalFilename", new String(file2.getOriginalFilename().getBytes("ISO-8859-1"), StandardCharsets.UTF_8));
        result.put("params", request.getParameterMap());
        result.put("user", user);
        return result;
    }

    static class User {
        private String userName;
        private int age;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
