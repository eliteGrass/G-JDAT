package com.itsoku.lesson007.service;

import com.itsoku.lesson007.mapper.Lesson007Mapper;
import com.itsoku.lesson007.po.Lesson007PO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/3/31 22:04 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class Lesson007Service {
    @Autowired
    private Lesson007Mapper lesson007Mapper;

    /**
     * 声明式事务，事务范围比较大
     *
     * @throws InterruptedException
     */
    @Transactional
    public void bigTransaction() throws InterruptedException {
        // 1、getData()方法模拟一个比较耗时的获取数据的操作，这个方法内部会休眠5秒
        String data = this.getData();

        //2、将上面获取到的数据写入到db中
        Lesson007PO po = new Lesson007PO();
        po.setId(UUID.randomUUID().toString());
        po.setData(data);
        this.lesson007Mapper.insert(po);
    }

    public String getData() throws InterruptedException {
        //休眠5秒
        TimeUnit.SECONDS.sleep(5);
        return UUID.randomUUID().toString();
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 使用 TransactionTemplate 编程式事务，可以灵活的控制事务的范围
     *
     * @throws InterruptedException
     */
    public void smallTransaction() throws InterruptedException {
        // 1、调用getData()方法，讲获取的数据写到db中，假设 getData方法比较耗时，比如耗时 5秒
        String data = this.getData();

        //2、将上面获取到的数据写入到db中
        Lesson007PO po = new Lesson007PO();
        po.setId(UUID.randomUUID().toString());
        po.setData(data);

        // this.transactionTemplate.executeWithoutResult可以传入一个Consumer，这个Consumer表述需要在事务中执行的业务操作
        this.transactionTemplate.executeWithoutResult(action -> {
            this.lesson007Mapper.insert(po);
        });
    }


}
