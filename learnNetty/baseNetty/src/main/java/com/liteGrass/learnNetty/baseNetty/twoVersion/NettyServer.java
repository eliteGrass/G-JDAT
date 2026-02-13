package com.liteGrass.learnNetty.baseNetty.twoVersion;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: NettyServer
 * @Author: liteGrass
 * @Date: 2025/8/25 22:54
 * @Description: 改进版本reactor模型的
 */
public class NettyServer {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        ServerBootstrap channel = bootstrap.channel(NioServerSocketChannel.class);
        // 创建boss以及worker
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(3);
        channel.group(bossGroup, workerGroup);
        // 创建处理器工作线程
        DefaultEventLoopGroup handlerGroup = new DefaultEventLoopGroup(3);

        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(handlerGroup, new StringDecoder());
                channel.pipeline().addLast(handlerGroup, new ChannelInboundHandlerAdapter() {

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.info("receive msg: " + msg);
                    }
                });
            }
        });

        // 绑定端口号
        bootstrap.bind(8000);
    }
}
