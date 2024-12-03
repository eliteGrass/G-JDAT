package com.liteGrass.mq.tools.config;


import cn.hutool.setting.dialect.Props;
import cn.hutool.setting.dialect.PropsUtil;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;

/**
 * @Description 基础版本mq配置信息
 * @Author liteGrass
 * @Date 2024/11/28 17:13
 */
public class BaseMqConfig {

    private static final String IP_NAME = "mq.proxy.ip";
    private static final String PORT_NAME = "mq.proxy.port";

    private static final String endpoints;
    private static final String ip;
    private static final String port;

    static {
        Props props = PropsUtil.get("baseMq.properties");
        ip = props.getStr(IP_NAME);
        port = props.getStr(PORT_NAME);
        endpoints = ip + ":" + port;
    }

    public static ClientConfiguration getClientConfiguration() {
        return ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .build();
    }

    public static ClientServiceProvider getClientServiceProvider() {
        return ClientServiceProvider.loadService();
    }



}
