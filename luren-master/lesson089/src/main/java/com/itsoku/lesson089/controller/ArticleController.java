package com.itsoku.lesson089.controller;

import com.itsoku.lesson089.mapper.ArticleMapper;
import com.itsoku.lesson089.po.ArticlePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/9/9 21:01 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@RestController
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 根据文章标题模糊检索我的文章列表
     *
     * @param myUserId 用户id，这里只是为了方便测试，所以加了这个参数，现实中这个值是从session中获取的，不是传进来的
     * @param keywords 关键字，按照文章标题模糊检索
     * @return
     */
    @GetMapping("/findMyArticles1")
    public List<ArticlePO> findMyArticles1(@RequestParam("myUserId") Long myUserId, @RequestParam("keywords") String keywords) {
        List<ArticlePO> result = this.articleMapper.findMyArticles1(myUserId, keywords);
        return result;
    }

    /**
     * 根据文章标题模糊检索我的文章列表
     *
     * @param myUserId 用户id，这里只是为了方便测试，所以加了这个参数，现实中这个值是从session中获取的，不是传进来的
     * @param keywords 关键字，按照文章标题模糊检索
     * @return
     */
    @GetMapping("/findMyArticles2")
    public List<ArticlePO> findMyArticles2(@RequestParam("myUserId") Long myUserId, @RequestParam("keywords") String keywords) {
        List<ArticlePO> result = this.articleMapper.findMyArticles2(myUserId, "%" + keywords + "%");
        return result;
    }
}
