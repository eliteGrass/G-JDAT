package com.itsoku.lesson042.common.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * SystemContext 持有者
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/5/27 14:39 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Slf4j
public class SystemContextHolder {
    private static final ThreadLocal<SystemContext> SYSTEM_CONTEXT_THREAD_LOCAL = new ThreadLocal<SystemContext>() {
        @Override
        protected SystemContext initialValue() {
            return new SystemContext();
        }
    };

    public static SystemContext getSystemContext() {
        return SYSTEM_CONTEXT_THREAD_LOCAL.get();
    }

    public static void setSystemContext(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            // 并不是所有头都会放入上下文，有效的头才会被放入上下文
            if (isValid(headerName)) {
                String header = request.getHeader(headerName);
                getSystemContext().set(headerName, decodeValue(header));
            }
        }
    }

    /**
     * 对中文字符做url decode
     *
     * @param value header value
     */
    private static String decodeValue(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            if (log.isDebugEnabled()) {
                log.error("can't convert value:" + value + " to UTF-8,will set the raw value");
            }
            return value;
        }
    }

    /**
     * key是否有效
     *
     * @param key
     * @return
     */
    public static boolean isValid(String key) {
        // 这里必须是 itsoku-header- 开头的才是有效的
        return key != null && StringUtils.startsWithIgnoreCase(key, SystemContext.CONTEXT_PREFIX);
    }

}
