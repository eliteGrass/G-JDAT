package com.liteGrass.es.v7;

import com.liteGrass.es.entity.Employee;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description V7版本测试类
 * @Author liteGrass
 * @Date 2024/12/16 22:17
 */
@SpringBootTest
public class EsV7Test {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void testMethodCreateIndex() {
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(Employee.class);
        System.out.println(indexOperations.exists());
        if (!indexOperations.exists()) {
            // 这种方式进行创建的不会设置相关配置setting\mapping等
            indexOperations.create();
        }
    }

    @Test
    public void testMethodDeleteIndex() {
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(Employee.class);
        indexOperations.delete();
    }


    // 文档操作  --------------------- 批量插入
    @Test
    public void testMethodBulkIndex() {

    }

    @Test
    public void testMethodQueryDocument() {

    }

}
