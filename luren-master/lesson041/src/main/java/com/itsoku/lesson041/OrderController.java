package com.itsoku.lesson041;

import com.itsoku.lesson041.common.Result;
import com.itsoku.lesson041.common.ResultUtils;
import com.itsoku.lesson041.dto.OrderPayReq;
import com.itsoku.lesson041.lock.DistributeLock;
import com.itsoku.lesson041.lock.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/23 23:09 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
@Slf4j
public class OrderController {
    @Autowired
    private DistributeLock distributeLock;

    /**
     * 模拟订单支付，为了防止用户重复支付，这里加了分布式锁，防止重复点击处理
     *
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping("/orderPay")
    @Lock(lockName = "'orderPay:'+ #req.orderId")
    public Result<String> orderPay(@Validated @RequestBody OrderPayReq req) throws Exception {
        //休眠10秒，模拟业务操作
        TimeUnit.SECONDS.sleep(10);
        return ResultUtils.success("支付成功");
    }

}
