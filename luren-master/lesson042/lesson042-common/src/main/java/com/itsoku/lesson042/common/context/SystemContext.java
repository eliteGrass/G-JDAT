package com.itsoku.lesson042.common.context;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定义了一个系统上下文对象，里面有个Map，用来存放公共参数
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 14:33 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class SystemContext {
    private Logger logger = LoggerFactory.getLogger(SystemContext.class);
    public static final String CONTEXT_PREFIX = "itsoku-header-";
    public static final String CONTEXT_USER_ID = CONTEXT_PREFIX + "user_id";
    public static final String CONTEXT_USER_NAME = CONTEXT_PREFIX + "user_name";

    private final Map<String, String> contextMap;

    public SystemContext() {
        this.contextMap = new CaseInsensitiveMap<>();
    }

    public Map<String, String> getContextMap() {
        return contextMap;
    }

    public String get(String key) {
        return contextMap.get(key);
    }

    public void set(String key, String value) {
        contextMap.put(key, value);
    }

    public String getUserId() {
        return get(CONTEXT_USER_ID);
    }

    public void setUserId(String userId) {
        set(CONTEXT_USER_ID, userId);
    }

    public String getUserName() {
        return get(CONTEXT_USER_NAME);
    }

    public void setUserName(String userName) {
        set(CONTEXT_USER_NAME, userName);
    }

    public List<Pair<String, String>> toHeaders() {
        List<Pair<String, String>> result = new ArrayList<>(contextMap.size());
        contextMap.forEach((key, value) -> {
            Pair<String, String> pair;
            try {
                pair = Pair.of(key, URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                logger.error("can't convert value:" + value + " to UTF-8,will set the raw value");
                pair = Pair.of(key, value);
            }
            result.add(pair);
        });
        return result;
    }

}
