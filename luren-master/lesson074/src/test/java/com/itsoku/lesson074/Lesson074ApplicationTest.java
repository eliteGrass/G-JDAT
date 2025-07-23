package com.itsoku.lesson074;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/18 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@SpringBootTest
@Slf4j
public class Lesson074ApplicationTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 使用循环执行100个命令，看看耗时
     */
    @Test
    public void test1() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            keys.add("key" + i);
            values.add("value" + i);
        }

        //为了看到一个比较正常的性能，这里会测试10次，看最好的时间
        for (int j = 0; j < 10; j++) {
            //将上面100个key和对应的value通过循环丢到redis中
            long st = System.currentTimeMillis();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = values.get(i);
                this.stringRedisTemplate.opsForValue().set(key, value);
            }
            log.info("耗时(ms)：{}", (System.currentTimeMillis() - st));
        }
    }

    /**
     * 使用pipeline执行100个命令，看看耗时
     */
    @Test
    public void test2() {
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            keys.add("key-" + i);
            values.add("value-" + i);
        }

        //测试10次，看最好的时间
        for (int i = 0; i < 10; i++) {

            long st = System.currentTimeMillis();

            //将上面100个key和对应的value，通过pipelined打包一次性发给redis执行
            this.stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                    for (int i = 0; i < keys.size(); i++) {
                        String key = keys.get(i);
                        String value = values.get(i);
                        operations.opsForValue().set((K) key, (V) value);
                    }
                    return null;
                }
            });
            log.info("耗时(ms)：{}", (System.currentTimeMillis() - st));
        }
    }


    /**
     * pipeline中可以同时执行任何命令，下面演示下在pipeline中同时执行几个不同类型的命令
     */
    @Test
    public void test3() {
        /**
         * 使用pipeline执行4个命令，并拿到命令的返回值（这里返回值的数量和命令的数量可能不一样，这个什么意思呢？等会大家看结果就知道了）
         */
        List<Object> result = this.stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
                //执行set命令
                stringRedisTemplate.opsForValue().set("username", "路人");
                //向集合lesson中丢入3个数据
                stringRedisTemplate.opsForList().rightPushAll("lesson", "高并发", "微服务", "性能调优");
                //读取username的值
                stringRedisTemplate.opsForValue().get("username");
                //读取集合lesson中前3个元素的值
                stringRedisTemplate.opsForList().range("lesson", 0, 2);
                return null;
            }
        });
        /**
         * 输出命令返回结果，注意观察结果，上面执行了4个命令，但是result中有3个元素，这块大家一定要注意
         * 1、第1个命令（set命令）是没有返回值的
         * 2、result中第1元素是第2个命令的返回值
         * 3、result中第2元素是第3个命令的返回值
         * 4、result中第3元素是第4个命令的返回值
         */
        log.info("{}", JSONUtil.toJsonPrettyStr(result));
    }

}
