package com.liteGrass.learnNetty.baseNetty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: FirstNettyServer
 * @Author: liteGrass
 * @Date: 2025/7/31 20:19
 * @Description: 第一个netty程序
 */
public class FirstNettyServer {

    private static final Logger log = LoggerFactory.getLogger(FirstNettyServer.class);

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
        // 绑定端口号
        serverBootstrap.bind(8000);
    }

}
