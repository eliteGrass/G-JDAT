package com.liteGrass.learnNetty.baseNetty.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @ClassName: NettyFirstDemo
 * @Author: liteGrass
 * @Date: 2025/7/31 19:45
 * @Description: 第一个netty测试程序
 */
public class NettyFirstDemo {

    private static final Logger log = LoggerFactory.getLogger(NettyFirstDemo.class);


    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        ServerBootstrap channel = serverBootstrap.channel(NioServerSocketChannel.class);

        // 相当于进入死循环进行监听相关事件
        channel.group(new NioEventLoopGroup());

        // 进行一系列的事件处理
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            // 初始化channel
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                // 添加解码相关的channel
                nioSocketChannel.pipeline().addLast(new StringDecoder());
                nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.info("获取到相关信息:{}", msg);
                    }
                });
            }
        });

        serverBootstrap.bind(8000);
        System.out.println();
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 19:45
    * @Desc: netty服务端
    */
    @Test
    void testMethodServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        ServerBootstrap channel = serverBootstrap.channel(NioServerSocketChannel.class);

        // 相当于进入死循环进行监听相关事件
        channel.group(new NioEventLoopGroup());

        // 进行一系列的事件处理
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            // 初始化channel
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                // 添加解码相关的channel
                nioSocketChannel.pipeline().addLast(new StringDecoder());
                nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.info("获取到相关信息:{}", msg);
                    }
                });
            }
        });

        serverBootstrap.bind(8000);
        System.out.println();
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/31 20:07
    * @Desc: 客户端服务
    */
    @Test
    void testMethodClient() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.channel(NioSocketChannel.class);

        // 他也是进行多线程操作的
        bootstrap.group(new NioEventLoopGroup());

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
