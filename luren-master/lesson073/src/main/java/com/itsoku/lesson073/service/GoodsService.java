package com.itsoku.lesson073.service;

import cn.hutool.core.util.IdUtil;
import com.itsoku.lesson073.mapper.GoodsMapper;
import com.itsoku.lesson073.mapper.OrderMapper;
import com.itsoku.lesson073.po.GoodsPO;
import com.itsoku.lesson073.po.OrderPO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/8/13 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Service
public class GoodsService {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 下单，先扣库存，然后插入订单
     *
     * @param goodsId 商品id
     * @param num     下单量
     */
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(String goodsId, int num) {
        //为了防止超卖，这里使用分布式锁解决的，加锁成功后，扣减库存，然后插入订单
        //1、获取分布式锁
        RLock lock = this.redissonClient.getLock("placeOrder:" + goodsId);
        //2、加锁成功
        lock.lock();
        try {
            //3、扣减库存：从db中查询出商品，若库存不够，直接抛出异常；否则扣减库存，并将商品信息更新到db
            GoodsPO goodsPO = this.goodsMapper.selectById(goodsId);
            if (goodsPO.getStock() < num) {
                throw new RuntimeException("库存不足");
            }
            goodsPO.setStock(goodsPO.getStock() - num);
            this.goodsMapper.updateById(goodsPO);

            //4、向订单表写入数据
            OrderPO orderPO = OrderPO.builder().id(IdUtil.fastSimpleUUID()).goodsId(goodsId).num(num).build();
            this.orderMapper.insert(orderPO);
        } finally {
            //释放锁，锁释放后，数据事务并没有提交，此时其他线程会获取锁成功，进来看到的库存，并不是最新的库存，就可能出现超卖的风险
            lock.unlock();
        }
    }


    /**
     * 下单
     *
     * @param goodsId 商品id
     * @param num     下单量
     */
    @Transactional(rollbackFor = Exception.class)
    public void placeOrder1(String goodsId, int num) {
        //1、获取分布式锁
        RLock lock = this.redissonClient.getLock("placeOrder:" + goodsId);
        //2、加锁成功
        lock.lock();
        try {
            //3、扣减库存：从db中查询出商品，若库存不够，直接抛出异常；否则扣减库存，并将商品信息更新到db
            GoodsPO goodsPO = this.goodsMapper.selectById(goodsId);
            if (goodsPO.getStock() < num) {
                throw new RuntimeException("库存不足");
            }
            goodsPO.setStock(goodsPO.getStock() - num);
            this.goodsMapper.updateById(goodsPO);

            //4、向订单表写入数据
            OrderPO orderPO = OrderPO.builder().id(IdUtil.fastSimpleUUID()).goodsId(goodsId).num(num).build();
            this.orderMapper.insert(orderPO);
        } finally {
            /**
             * 释放锁，锁释放后，数据事务并没有提交，此时其他线程会获取锁成功，进来看到的库存，并不是最新的库存，就可能出现超卖的风险
             */
            lock.unlock();
        }
        //假如数据提交事务的时候，比较慢，比如耗时了200毫秒
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 下单
     *
     * @param goodsId 商品id
     * @param num     下单量
     */
    public void placeOrder2(String goodsId, int num) {
        //1、获取分布式锁
        RLock lock = this.redissonClient.getLock("placeOrder:" + goodsId);
        //2、加锁成功
        lock.lock();
        try {
            this.transactionTemplate.executeWithoutResult(action -> {
                //3、扣减库存：从db中查询出商品，若库存不够，直接抛出异常；否则扣减库存，并将商品信息更新到db
                GoodsPO goodsPO = this.goodsMapper.selectById(goodsId);
                if (goodsPO.getStock() < num) {
                    throw new RuntimeException("库存不足");
                }
                goodsPO.setStock(goodsPO.getStock() - num);
                this.goodsMapper.updateById(goodsPO);

                //4、向订单表写入数据
                OrderPO orderPO = OrderPO.builder().id(IdUtil.fastSimpleUUID()).goodsId(goodsId).num(num).build();
                this.orderMapper.insert(orderPO);
            });
        } finally {
            //释放锁
            lock.unlock();
        }
    }
}
