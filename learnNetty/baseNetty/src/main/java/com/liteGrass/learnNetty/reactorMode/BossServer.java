package com.liteGrass.learnNetty.reactorMode;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassName: BossServer
 * @Author: liteGrass
 * @Date: 2025/7/18 22:10
 * @Description: 主线程
 */
public class BossServer {

    private static final Logger log = LoggerFactory.getLogger(BossServer.class);

    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);

        // 创建监听器
        Selector bossSelector = Selector.open();
        SelectionKey register = ssc.register(bossSelector, 0);
        register.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("boss --- 开始创建工作线程");
        WorkerServer workerServer = new WorkerServer("工作线程-1");
        log.debug("boss --- 创建工作线程成功");
        while (true) {
            log.debug("boss --- 监听开始");
            bossSelector.select();
            log.debug("boss --- 监听成功");
            Set<SelectionKey> selectionKeys = bossSelector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    log.debug("boss --- 监听成功---开始连接");
                    SocketChannel sc = server.accept();
                    sc.configureBlocking(false);
                    log.debug("boss --- 监听成功---成功连接");
                    // 开启时开启一新的线程
                    workerServer.register(sc);
                }
            }

        }
    }

}
