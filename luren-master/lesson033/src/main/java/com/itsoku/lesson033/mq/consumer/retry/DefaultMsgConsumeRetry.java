package com.itsoku.lesson033.mq.consumer.retry;

import com.itsoku.lesson033.mq.enums.DelayLevelEnums;
import com.itsoku.lesson033.mq.po.MsgConsumePO;
import org.apache.commons.lang3.Range;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/4/29 13:09 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class DefaultMsgConsumeRetry implements MsgConsumeRetry {
    private static Map<Range<Integer>, DelayLevelEnums> retryDelayLevelMap = new LinkedHashMap<>();


    static {
        retryDelayLevelMap.put(Range.between(0, 5), DelayLevelEnums.SECOND_10);
        retryDelayLevelMap.put(Range.between(6, 10), DelayLevelEnums.SECOND_30);
        retryDelayLevelMap.put(Range.between(11, 15), DelayLevelEnums.MINUTE_1);
        retryDelayLevelMap.put(Range.between(16, 20), DelayLevelEnums.MINUTE_5);
        retryDelayLevelMap.put(Range.between(21, 50), DelayLevelEnums.MINUTE_10);
    }

    private long getDelayTimeInMills(int failCount) {
        for (Map.Entry<Range<Integer>, DelayLevelEnums> entry : retryDelayLevelMap.entrySet()) {
            Range<Integer> range = entry.getKey();
            if (range.contains(failCount)) {
                return entry.getValue().getDelayTimeInMills();
            }
        }
        return DelayLevelEnums.HOUR_1.getDelayTimeInMills();
    }

    //默认重试次数
    private static final Integer MAX_RETRY_COUNT = 50;

    @Override
    public MsgConsumeRetryResult getRetryResult(MsgConsumePO msgConsumerPO) {
        MsgConsumeRetryResult mqSendRetryResult = new MsgConsumeRetryResult();
        //当前失败次数 < 最大重试次数，则可以继续重试
        if (msgConsumerPO.getFailCount() < MAX_RETRY_COUNT) {
            //获取延迟时间
            long delayTimeInMills = this.getDelayTimeInMills(msgConsumerPO.getFailCount());
            //下次重试时间（当前时间+延迟时间）
            LocalDateTime nextRetryTime = LocalDateTime.now().plus(Duration.ofMillis(delayTimeInMills));
            mqSendRetryResult.setNextRetryTime(nextRetryTime);
            mqSendRetryResult.setRetry(true);
        } else {
            mqSendRetryResult.setRetry(false);
        }
        return mqSendRetryResult;
    }
}
