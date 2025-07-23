package com.itsoku.lesson078.controller;

import com.itsoku.lesson078.dto.BookDto;
import com.itsoku.lesson078.dto.UserDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/23 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Controller
public class RestTemplateTestController {

    @GetMapping("/test/get")
    @ResponseBody
    public BookDto get() {
        return new BookDto(1, "高并发 & 微服务 & 性能调优实战案例 100 讲");
    }

    @GetMapping("/test/get/{id}/{name}")
    @ResponseBody
    public BookDto get(@PathVariable("id") Integer id, @PathVariable("name") String name) {
        return new BookDto(id, name);
    }

    @GetMapping("/test/getList")
    @ResponseBody
    public List<BookDto> getList() {
        return Arrays.asList(
                new BookDto(1, "Spring高手系列"),
                new BookDto(2, "高并发 & 微服务 & 性能调优实战案例 100 讲")
        );
    }

    /**
     * 下载文件
     *
     * @return
     */
    @GetMapping("/test/downFile")
    @ResponseBody
    public HttpEntity<InputStreamResource> downFile() {
        //将文件流封装为InputStreamResource对象
        InputStream inputStream = this.getClass().getResourceAsStream("/1.txt");
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        //设置header
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=1.txt");
        HttpEntity<InputStreamResource> httpEntity = new HttpEntity<>(inputStreamResource);
        return httpEntity;
    }

    @GetMapping("/test/header")
    @ResponseBody
    public Map<String, List<String>> header(HttpServletRequest request) {
        Map<String, List<String>> header = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            List<String> list = new ArrayList<>();
            while (values.hasMoreElements()) {
                list.add(values.nextElement());
            }
            header.put(name, list);
        }
        return header;
    }

    @GetMapping("/test/getAll/{path1}/{path2}")
    @ResponseBody
    public Map<String, Object> getAll(@PathVariable("path1") String path1,
                                      @PathVariable("path2") String path2,
                                      HttpServletRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("path1", path1);
        result.put("path2", path2);
        //头
        Map<String, List<String>> header = new LinkedHashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            List<String> list = new ArrayList<>();
            while (values.hasMoreElements()) {
                list.add(values.nextElement());
            }
            header.put(name, list);
        }
        result.put("header", header);
        return result;
    }

    @PostMapping("/test/form1")
    @ResponseBody
    public BookDto form1(BookDto bookDto) {
        return bookDto;
    }

    @PostMapping(value = "/test/form2")
    @ResponseBody
    public Map<String, String> form2(@RequestParam("file1") MultipartFile file1) {
        Map<String, String> fileMetadata = new LinkedHashMap<>();
        fileMetadata.put("文件名", file1.getOriginalFilename());
        fileMetadata.put("文件类型", file1.getContentType());
        fileMetadata.put("文件大小(byte)", String.valueOf(file1.getSize()));
        return fileMetadata;
    }

    /**
     * 复杂的表单：包含了普通元素、多文件
     *
     * @param userDto
     * @return
     */
    @PostMapping("/test/form3")
    @ResponseBody
    public Map<String, String> form3(UserDto userDto) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("name", userDto.getName());
        result.put("headImg", userDto.getHeadImg().getOriginalFilename());
        result.put("idImgList", Arrays.toString(userDto.getIdImgList().stream().
                map(MultipartFile::getOriginalFilename).toArray()));
        return result;
    }

    /**
     * body中json格式的数据，返回值非泛型
     *
     * @param bookDto
     * @return
     */
    @PostMapping("/test/form4")
    @ResponseBody
    public BookDto form4(@RequestBody BookDto bookDto) {
        return bookDto;
    }

    /**
     * body中json格式的数据，返回值为泛型
     *
     * @param bookDtoList
     * @return
     */
    @PostMapping("/test/form5")
    @ResponseBody
    public List<BookDto> form5(@RequestBody List<BookDto> bookDtoList) {
        return bookDtoList;
    }


}
