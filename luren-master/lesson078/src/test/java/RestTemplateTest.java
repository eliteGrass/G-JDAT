import com.itsoku.lesson078.dto.BookDto;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/23 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class RestTemplateTest {
    @Test
    public void test1() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/get";
        //getForObject方法，获取响应体，将其转换为第二个参数指定的类型
        BookDto bookDto = restTemplate.getForObject(url, BookDto.class);
        System.out.println(bookDto);
    }

    @Test
    public void test2() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/get";
        //getForEntity方法，返回值为ResponseEntity类型
        // ResponseEntity中包含了响应结果中的所有信息，比如头、状态、body
        ResponseEntity<BookDto> responseEntity = restTemplate.getForEntity(url, BookDto.class);
        //状态码
        System.out.println(responseEntity.getStatusCode());
        //获取头
        System.out.println("头：" + responseEntity.getHeaders());
        //获取body
        BookDto bookDto = responseEntity.getBody();
        System.out.println(bookDto);
    }

    @Test
    public void test3() {
        RestTemplate restTemplate = new RestTemplate();
        //url中有动态参数
        String url = "http://localhost:8080/test/get/{id}/{name}";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", "1");
        uriVariables.put("name", "高并发 & 微服务 & 性能调优实战案例 100 讲");
        //使用getForObject或者getForEntity方法
        BookDto bookDto = restTemplate.getForObject(url, BookDto.class, uriVariables);
        System.out.println(bookDto);
    }

    @Test
    public void test4() {
        RestTemplate restTemplate = new RestTemplate();
        //url中有动态参数
        String url = "http://localhost:8080/test/get/{id}/{name}";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", "1");
        uriVariables.put("name", "高并发 & 微服务 & 性能调优实战案例 100 讲");
        //getForEntity方法
        ResponseEntity<BookDto> responseEntity = restTemplate.getForEntity(url, BookDto.class, uriVariables);
        BookDto bookDto = responseEntity.getBody();
        System.out.println(bookDto);
    }

    @Test
    public void test5() {
        RestTemplate restTemplate = new RestTemplate();
        //返回值为泛型
        String url = "http://localhost:8080/test/getList";
        //若返回结果是泛型类型的，需要使用到exchange方法，
        // 这个方法中有个参数是ParameterizedTypeReference类型，通过这个参数类指定泛型类型
        ResponseEntity<List<BookDto>> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<BookDto>>() {
                        });
        List<BookDto> bookDtoList = responseEntity.getBody();
        System.out.println(bookDtoList);
    }

    @Test
    public void test6() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/downFile";
        //文件比较小的情况，直接返回字节数组
        ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
        //获取文件的内容
        byte[] body = responseEntity.getBody();
        String content = new String(body);
        System.out.println(content);
    }

    @Test
    public void test7() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/downFile";
        /**
         * 文件比较大的时候，比如好几个G，就不能返回字节数组了，会把内存撑爆，导致OOM
         * 需要这么玩：
         * 需要使用execute方法了，这个方法中有个ResponseExtractor类型的参数，
         * restTemplate拿到结果之后，会回调{@link ResponseExtractor#extractData}这个方法，
         * 在这个方法中可以拿到响应流，然后进行处理，这个过程就是变读边处理，不会导致内存溢出
         */
        String result = restTemplate.execute(url,
                HttpMethod.GET,
                null,
                new ResponseExtractor<String>() {
                    @Override
                    public String extractData(ClientHttpResponse response) throws IOException {
                        System.out.println("状态：" + response.getStatusCode());
                        System.out.println("头：" + response.getHeaders());
                        //获取响应体流
                        InputStream body = response.getBody();
                        //处理响应体流
                        String content = IOUtils.toString(body, "UTF-8");
                        return content;
                    }
                }, new HashMap<>());

        System.out.println(result);
    }

    @Test
    public void test8() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/header";
        //①：请求头放在HttpHeaders对象中
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("header-1", "V1");
        headers.add("header-2", "Spring");
        headers.add("header-2", "SpringBoot");
        //②：RequestEntity：请求实体，请求的所有信息都可以放在RequestEntity中，比如body部分、头、请求方式、url等信息
        RequestEntity requestEntity = new RequestEntity(
                null, //body部分数据
                headers, //头
                HttpMethod.GET,//请求方法
                URI.create(url) //地址
        );
        ResponseEntity<Map<String, List<String>>> responseEntity = restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<Map<String, List<String>>>() {
                });
        Map<String, List<String>> result = responseEntity.getBody();
        System.out.println(result);
    }

    @Test
    public void test9() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/getAll/{path1}/{path2}";
        //①：请求头
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("header-1", "V1");
        headers.add("header-2", "Spring");
        headers.add("header-2", "SpringBoot");
        //②：url中的2个参数
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("path1", "v1");
        uriVariables.put("path2", "v2");
        //③：HttpEntity：HTTP实体，内部包含了请求头和请求体
        HttpEntity requestEntity = new HttpEntity(
                null,//body部分，get请求没有body，所以为null
                headers //头
        );
        //④：使用exchange发送请求
        ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                url, //url
                HttpMethod.GET, //请求方式
                requestEntity, //请求实体（头、body）
                new ParameterizedTypeReference<Map<String, Object>>() {
                },//返回的结果类型
                uriVariables //url中的占位符对应的值
        );
        Map<String, Object> result = responseEntity.getBody();
        System.out.println(result);
    }

    @Test
    public void test10() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form1";
        //①：表单信息，需要放在MultiValueMap中，MultiValueMap相当于Map<String,List<String>>
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //调用add方法填充表单数据(表单名称:值)
        body.add("id", "1");
        body.add("name", "高并发 & 微服务 & 性能调优实战案例 100 讲");
        //②：发送请求(url,请求体，返回值需要转换的类型)
        BookDto result = restTemplate.postForObject(url, body, BookDto.class);
        System.out.println(result);
    }

    @Test
    public void test11() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form1";
        //①：表单信息，需要放在MultiValueMap中，MultiValueMap相当于Map<String,List<String>>
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        //调用add方法放入表单元素(表单名称:值)
        body.add("id", "1");
        body.add("name", "高并发 & 微服务 & 性能调优实战案例 100 讲");
        //②：请求头
        HttpHeaders headers = new HttpHeaders();
        //调用set方法放入请求头
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        //③：请求实体：包含了请求体和请求头
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
        //④：发送请求(url,请求实体，返回值需要转换的类型)
        BookDto result = restTemplate.postForObject(url, httpEntity, BookDto.class);
        System.out.println(result);
    }


    @Test
    public void test12() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form2";
        //①：表单信息，需要放在MultiValueMap中，MultiValueMap相当于Map<String,List<String>>
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        //调用add方法放入表单元素(表单名称:值)
        //②：文件对应的类型，需要是org.springframework.core.io.Resource类型的，常见的有[InputStreamResource,ByteArrayResource]
        String filePath = ".\\lesson078\\src\\main\\java\\com\\itsoku\\lesson078\\dto\\UserDto.java";
        body.add("file1", new FileSystemResource(filePath));
        //③：头
        HttpHeaders headers = new HttpHeaders();
        headers.add("header1", "v1");
        headers.add("header2", "v2");
        //④：请求实体
        RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        //⑤：发送请求(请求实体，返回值需要转换的类型)
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        Map<String, String> result = responseEntity.getBody();
        System.out.println(result);
    }

    @Test
    public void test13() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form2";
        //①：表单信息，需要放在MultiValueMap中，MultiValueMap相当于Map<String,List<String>>
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        /**
         * ②：通过流的方式上传文件，流的方式需要用到InputStreamResource类，需要重写2个方法
         * getFilename：文件名称
         * contentLength：长度
         */
        InputStream inputStream = RestTemplateTest.class.getResourceAsStream("/1.txt");
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream) {
            @Override
            public String getFilename() {
                return "1.txt";
            }

            @Override
            public long contentLength() throws IOException {
                return inputStream.available();
            }
        };
        body.add("file1", inputStreamResource);
        //③：头
        HttpHeaders headers = new HttpHeaders();
        headers.add("header1", "v1");
        headers.add("header2", "v2");
        //④：请求实体
        RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        //⑤：发送请求(请求实体，返回值需要转换的类型)
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        Map<String, String> result = responseEntity.getBody();
        System.out.println(result);
    }

    @Test
    public void test14() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form3";
        //①：表单信息，需要放在MultiValueMap中，MultiValueMap相当于Map<String,List<String>>
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("name", "路人");
        body.add("headImg", new FileSystemResource(".\\lesson078\\src\\main\\resources\\1.jpg"));
        //来2张证件照，元素名称一样
        body.add("idImgList", new FileSystemResource(".\\lesson078\\src\\main\\resources\\2.jpg"));
        body.add("idImgList", new FileSystemResource(".\\lesson078\\src\\main\\resources\\3.jpg"));
        //③：头
        HttpHeaders headers = new HttpHeaders();
        headers.add("header1", "v1");
        headers.add("header2", "v2");
        //④：请求实体
        RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        //⑤：发送请求(请求实体，返回值需要转换的类型)
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<Map<String, String>>() {
                });
        Map<String, String> result = responseEntity.getBody();
        System.out.println(result);
    }

    @Test
    public void test15() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form4";
        BookDto body = new BookDto(1, "高并发 & 微服务 & 性能调优实战案例 100 讲");
        BookDto result = restTemplate.postForObject(url, body, BookDto.class);
        System.out.println(result);
    }

    @Test
    public void test16() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form5";
        //①：请求体，发送的时候会被转换为json格式数据
        List<BookDto> body = Arrays.asList(
                new BookDto(1, "高并发 & 微服务 & 性能调优实战案例 100 讲"),
                new BookDto(2, "MySQL系列"));
        //②：头
        HttpHeaders headers = new HttpHeaders();
        headers.add("header1", "v1");
        headers.add("header2", "v2");
        //③：请求实体
        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, URI.create(url));
        //④：发送请求(请求实体，返回值需要转换的类型)
        ResponseEntity<List<BookDto>> responseEntity = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<List<BookDto>>() {
                });
        //⑤：获取结果
        List<BookDto> result = responseEntity.getBody();
        System.out.println(result);
    }

    @Test
    public void test17() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/test/form5";
        //①：请求体为一个json格式的字符串
        String body = "[{\"id\":1,\"name\":\"高并发 & 微服务 & 性能调优实战案例 100 讲\"},{\"id\":2,\"name\":\"MySQL系列\"}]";
        /**
         * ②：若请求体为json字符串的时候，需要在头中设置Content-Type=application/json；
         * 若body是普通的java类的时候，无需指定这个，RestTemplate默认自动配上Content-Type=application/json
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //③：请求实体（body，头、请求方式，uri）
        RequestEntity requestEntity = new RequestEntity(body, headers, HttpMethod.POST, URI.create(url));
        //④：发送请求(请求实体，返回值需要转换的类型)
        ResponseEntity<List<BookDto>> responseEntity = restTemplate.exchange(
                requestEntity,
                new ParameterizedTypeReference<List<BookDto>>() {
                });
        //⑤：获取结果
        List<BookDto> result = responseEntity.getBody();
        System.out.println(result);
    }

}
