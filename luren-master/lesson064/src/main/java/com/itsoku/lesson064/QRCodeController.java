package com.itsoku.lesson064;

import cn.hutool.extra.qrcode.QrCodeUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 第64节 使用hutool，生成&解析，二维码
 * <b>description</b>： Java高并发、微服务、性能优化实战案例100讲，视频号：程序员路人，源码 & 文档 & 技术支持，请加个人微信号：itsoku <br>
 * <b>time</b>：2024/7/17 20:13 <br>
 * <b>author</b>：ready likun_557@163.com
 */
@Controller
public class QRCodeController {

    /**
     * 生成二维码
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return
     */
    @GetMapping("/generateQrCode")
    @ResponseBody
    public ResponseEntity<byte[]> generateQrCode(@RequestParam("content") String content, @RequestParam("width") int width, @RequestParam("height") int height) {
        //生成二维码（hutool工具类QrCodeUtil）
        byte[] bytes = QrCodeUtil.generatePng(content, width, height);
        //返回二维码图片（png格式）
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE).body(bytes);
    }
}
