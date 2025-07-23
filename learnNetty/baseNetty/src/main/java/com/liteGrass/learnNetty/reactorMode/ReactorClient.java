package com.liteGrass.learnNetty.reactorMode;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @ClassName: ReactorClient
 * @Author: liteGrass
 * @Date: 2025/7/18 23:10
 * @Description: 客户端
 */
public class ReactorClient {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8080));
        socketChannel.configureBlocking(false);

        System.out.println("-------------------------------------------");

    }

}
