package com.itsoku.lesson082;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/30 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class FileController {

    @GetMapping("/download1")
    public void download1(HttpServletResponse response) throws IOException {
        // 指定要下载的文件
        File file = ResourceUtils.getFile("classpath:文件1.txt");
        // 文件转成字节数组
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        //文件名编码，防止中文乱码
        String filename = URLEncoder.encode(file.getName(), "UTF-8");
        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        // 内容类型为通用类型，表示二进制数据流
        response.setContentType("application/octet-stream");
        //输出文件内容
        try (OutputStream os = response.getOutputStream()) {
            os.write(fileBytes);
        }
    }

    @GetMapping("/download2")
    public ResponseEntity<byte[]> download2() throws IOException {
        // 指定要下载的文件
        File file = ResourceUtils.getFile("classpath:文件1.txt");
        // 文件转成字节数组
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        //文件名编码，防止中文乱码
        String filename = URLEncoder.encode(file.getName(), "UTF-8");
        //构建响应实体：ResponseEntity，ResponseEntity中包含了http请求的响应信息，比如状态码、头、body
        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(fileBytes);
        return responseEntity;
    }

    @GetMapping("/download3")
    public void download3(HttpServletResponse response) throws IOException {
        // 指定要下载的文件
        File file = ResourceUtils.getFile("classpath:文件1.txt");
        //文件名编码，防止中文乱码
        String filename = URLEncoder.encode(file.getName(), "UTF-8");
        // 设置响应头信息
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        // 内容类型为通用类型，表示二进制数据流
        response.setContentType("application/octet-stream");
        // 循环，边读取边输出，可避免大文件时OOM
        try (InputStream inputStream = new FileInputStream(file); OutputStream os = response.getOutputStream()) {
            byte[] bytes = new byte[1024];
            int readLength;
            while ((readLength = inputStream.read(bytes)) != -1) {
                os.write(bytes, 0, readLength);
            }
        }
    }

    @GetMapping("/download4")
    public ResponseEntity<Resource> download4() throws IOException {
        /**
         * 通过 ResponseEntity 下载文件，body 为 org.springframework.core.io.Resource 类型
         * Resource是spring中的一个资源接口，是对资源的一种抽象，常见的几个实现类
         * ClassPathResource：classpath下面的文件资源
         * FileSystemResource：文件系统资源
         * InputStreamResource：流资源
         * ByteArrayResource：字节数组资源
         */
        Resource resource = new ClassPathResource("文件1.txt");
        //文件名编码，防止中文乱码
        String filename = URLEncoder.encode("文件1.txt", "UTF-8");
        //构建响应实体：ResponseEntity，ResponseEntity中包含了http请求的响应信息，比如状态码、头、body
        ResponseEntity<Resource> responseEntity = ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE).body(resource);
        return responseEntity;
    }

}