package com.itsoku.utils;

import cn.hutool.crypto.digest.DigestUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <b>description</b>：签名工具类 <br>
 * <b>time</b>：2019-01-14 16:47 <br>
 * <b>author</b>： ready likun_557@163.com
 */
public class SignUtils {
    /**
     * 签名
     *
     * @param paramMap
     * @param key
     * @return
     */
    public static String createSign(Map<String, Object> paramMap, String key) {
        List<String> keyList = FrameUtils.newArrayList();
        for (String s : paramMap.keySet()) {
            keyList.add(s);
        }
        Collections.sort(keyList, String::compareTo);
        StringBuilder sb = new StringBuilder();
        for (String s : keyList) {
            Object obj = paramMap.get(s);
            if (obj != null) {
                if (obj.getClass().isArray()) {
                    Object[] obs = (Object[]) obj;
                    for (Object object : obs) {
                        sb.append(object.toString());
                    }
                } else {
                    sb.append(obj.toString());
                }
            }
        }
        sb.append(key);
        return DigestUtil.md5Hex(sb.toString());
    }

    /**
     * 验证签名是否有效
     *
     * @param paramMap
     * @param sign
     * @return
     */
    public static boolean validSign(Map<String, Object> paramMap, String key, String sign) {
        boolean result = createSign(paramMap, key).equals(sign);
        return result;
    }

}
