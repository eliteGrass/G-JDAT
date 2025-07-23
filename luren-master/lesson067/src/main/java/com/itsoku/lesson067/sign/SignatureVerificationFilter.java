package com.itsoku.lesson067.sign;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.itsoku.lesson067.common.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/22 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@WebFilter(urlPatterns = "/**", filterName = "SignatureVerificationFilter")
@Component
public class SignatureVerificationFilter extends OncePerRequestFilter {
    public static Logger logger = LoggerFactory.getLogger(SignatureVerificationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //对request进行包装，支持重复读取body
        ReusableBodyRequestWrapper requestWrapper = new ReusableBodyRequestWrapper(request);
        //校验签名
        if (this.verifySignature(requestWrapper, response)) {
            filterChain.doFilter(requestWrapper, response);
        }
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //签名秘钥
    @Value("${secretKey:b0e8668b-bcf2-4d73-abd4-893bbc1c6079}")
    private String secretKey;

    /**
     * 校验签名
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public boolean verifySignature(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //签名
        String sign = request.getHeader("X-Sign");
        //随机数
        String nonce = request.getHeader("X-Nonce");
        //时间戳
        String timestampStr = request.getHeader("X-Timestamp");
        if (StringUtils.isBlank(sign) || StringUtils.isBlank(nonce) || StringUtils.isBlank(timestampStr)) {
            this.write(response, "参数错误");
            return false;
        }

        //timestamp 10分钟内有效
        long timestamp = Long.parseLong(timestampStr);
        long currentTimestamp = System.currentTimeMillis() / 1000;
        if (Math.abs(currentTimestamp - timestamp) > 600) {
            this.write(response, "请求已过期");
            return false;
        }

        //防止请求重放，nonce只能用一次，放在redis中，有效期 20分钟
        String nonceKey = "SignatureVerificationFilter:nonce:" + nonce;
        if (!this.redisTemplate.opsForValue().setIfAbsent(nonceKey, "1", 20, TimeUnit.MINUTES)) {
            this.write(response, "nonce无效");
            return false;
        }

        //请求体
        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        //需要签名的数据：secretKey+noce+timestampStr+body
        //校验签名
        String data = String.format("%s%s%s%s", this.secretKey, nonce, timestampStr, body);
        if (!DigestUtil.md5Hex(data).equals(sign)) {
            write(response, "签名有误");
            return false;
        }
        return true;
    }

    private void write(HttpServletResponse response, String msg) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(JSONUtil.toJsonStr(ResultUtils.error(msg)));
    }
}
