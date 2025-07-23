package com.itsoku.lesson068;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AesEncryptDecryptExample {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws JsonProcessingException {
        // 密钥，AES 要求密钥长度为 128, 192, 256 位
        String key = "1234567890123456"; // 128位

        // 待加密的字符串
        String content = objectMapper.writeValueAsString(Arrays.asList("高并发", "微服务", "性能调优实战"));

        AES aes = SecureUtil.aes(key.getBytes(StandardCharsets.UTF_8));
        //加密
        String encrypt = aes.encryptHex(content);
        System.out.println("加密结果:" + encrypt);

        //解密
        String decrypt = aes.decryptStr("6c2747031ffa0d87a9eff93b740108ac0c931be782ed331cac3689b45d4382472f8cec0370425d0c8f8af88092183a8d2ede3852f0ebb66fa53ab691c435c7c9027893f646cecfcab45da5965742a0e24559c0c079f17306b9b11fc7f50a0952a4c242cdbe87c31d9aed10745fb84075");
        System.out.println("解密结果:" + decrypt);
    }
}