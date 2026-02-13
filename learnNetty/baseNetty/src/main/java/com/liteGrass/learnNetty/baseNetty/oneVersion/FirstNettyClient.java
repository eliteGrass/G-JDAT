package com.liteGrass.learnNetty.baseNetty.oneVersion;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @ClassName: FirstNettyClient
 * @Author: liteGrass
 * @Date: 2025/7/31 20:20
 * @Description: 第一个netty客户端程序
 */
public class FirstNettyClient {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class);

        // 他也是进行多线程操作的，连接一个线程然后处理器多个线程
        bootstrap.group(new NioEventLoopGroup());

        // 定义处理器
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new StringEncoder());
            }
        });

        // 进行连接
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
        connect.sync();

        Channel channel = connect.channel();
        channel.writeAndFlush("Hello World");
    }

}
