package com.itsoku.lesson065.controller;

import com.itsoku.lesson065.dto.UserPointsReq;
import com.itsoku.lesson065.dto.UserRanking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/18 20:23 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class UserRankingController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 用户积分批量插入到redis
     *
     * @param userPointsReqList
     * @return
     */
    @PostMapping("/addUserPoint")
    public boolean addUserPoint(@RequestBody List<UserPointsReq> userPointsReqList) {
        String key = "user:ranking";
        for (UserPointsReq userPointsReq : userPointsReqList) {
            String userId = userPointsReq.getUserId();
            //先按积分降序，积分相同时按照最后更新时间升序，score = 积分 + (1 - 时间戳/10的13次方)
            double score = userPointsReq.getPoints() + (1 - userPointsReq.getUpdateTime() / 1e13);
            this.stringRedisTemplate.opsForZSet().add(key, userId, score);
        }
        return true;
    }

    /**
     * 获取用户积分排行榜(倒序)
     *
     * @param topN 前多少名
     * @return
     */
    @GetMapping("/userRankings")
    public List<UserRanking> userRankings(@RequestParam("topN") int topN) {
        String key = "user:ranking";
        Set<ZSetOperations.TypedTuple<String>> typedTuples = this.stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, 0, topN - 1);

        List<UserRanking> userRankingList = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            UserRanking userRanking = new UserRanking();
            userRanking.setUserId(typedTuple.getValue());
            userRanking.setRedisScore(typedTuple.getScore());
            userRankingList.add(userRanking);
        }
        return userRankingList;
    }

}
