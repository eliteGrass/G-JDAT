package com.itsoku.lesson084.controller;

import com.itsoku.lesson084.common.Result;
import com.itsoku.lesson084.common.ResultUtils;
import com.itsoku.lesson084.dto.ArticlePublishRequest;
import com.itsoku.lesson084.service.ArticleService;
import com.itsoku.lesson084.service.ArticleServiceNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/2 20:15 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 发布文章，传统版本的事项
     *
     * @param req
     * @return
     */
    @PostMapping("/article/publish")
    public Result<String> publish(@RequestBody ArticlePublishRequest req) {
        this.articleService.publish(req);
        return ResultUtils.success("发布成功");
    }


    @Autowired
    private ArticleServiceNew articleServiceNew;

    /**
     * 发布文章，责任链版本实现，更容易扩展
     *
     * @param req
     * @return
     */
    @PostMapping("/article/publishNew")
    public Result<String> publishNew(@RequestBody ArticlePublishRequest req) {
        this.articleServiceNew.publish(req);
        return ResultUtils.success("发布成功");
    }
}
