package com.itsoku.lesson033.util;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import org.springframework.amqp.core.Message;

import java.io.UnsupportedEncodingException;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/7 15:20 <br>
 * <b>author</b>：ready likun_557@163.com
 */
public class MqUtils {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * 将消息体转换为对象
     *
     * @param message
     * @param bodyClass
     * @param <T>
     * @return
     */

    public static <T> T getBody(Message message, Class<T> bodyClass) {
        try {
            return JSONUtil.toBean(new String(message.getBody(), DEFAULT_CHARSET_NAME), bodyClass);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将消息体转换为对象
     *
     * @param message
     * @param typeReference
     * @param <T>
     * @return
     */
    public static <T> T getBody(Message message, TypeReference<T> typeReference) {
        try {
            return JSONUtil.toBean(new String(message.getBody(), DEFAULT_CHARSET_NAME), typeReference, false);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
