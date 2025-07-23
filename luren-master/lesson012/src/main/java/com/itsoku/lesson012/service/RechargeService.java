package com.itsoku.lesson012.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itsoku.lesson012.po.RechargePO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/4 23:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public interface RechargeService extends IService<RechargePO> {
    /**
     * 重置下订单，这个方法是个测试用例用的，在测试每种效果之前，将充值订单的状态还原一下，方便看效果用的
     *
     * @param rechargeId
     * @return
     */
    boolean reset(String rechargeId);

    /**
     * 充值回调，处理方式1：使用本身状态条件判断解决
     *
     * @param rechargeId
     * @return 若已处理成功过或处理成功，返回true，否则返回false
     */
    boolean rechargeCallBack1(String rechargeId);

    /**
     * 充值回调，处理方式2：采用乐观锁解决
     *
     * @param rechargeId
     * @return 若已处理成功过或处理成功，返回true，否则返回false
     */
    boolean rechargeCallBack2(String rechargeId);

    /**
     * 充值回调，处理方式3：使用通用幂等方案解决
     *
     * @param rechargeId
     * @return 若已处理成功过或处理成功，返回true，否则返回false
     */
    boolean rechargeCallBack3(String rechargeId);
}
