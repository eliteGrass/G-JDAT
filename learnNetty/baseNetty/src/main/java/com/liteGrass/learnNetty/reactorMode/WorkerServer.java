package com.liteGrass.learnNetty.reactorMode;


import cn.hutool.core.lang.Opt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ClassName: WorkerServer
 * @Author: liteGrass
 * @Date: 2025/7/18 22:16
 * @Description:
 */
public class WorkerServer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(WorkerServer.class);
    private Thread workerThread;
    private Selector selector;
    private String name;

    private ConcurrentLinkedQueue<Runnable> queue;

    public WorkerServer(String name) throws IOException {
        this.name = name;
        this.queue = new ConcurrentLinkedQueue<>();
        this.selector = Selector.open();
        this.workerThread =  new Thread(this, name);
        log.debug("worker --- 开始启动工作线程");
        workerThread.start();
    }

    public void register(SocketChannel sc) {
        log.debug("worker --- 工作线程register方法条用");
        queue.add(() -> {
            try {
                log.debug("worker --- 工作线程注册进行调用");
                sc.register(selector, SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                throw new RuntimeException(e);
            }
        });
        // 必须执行，当线程内部方法先执行的时候，卡死。由于有唤醒机制他会唤醒，该唤醒机制是无论卡死在唤醒之前你还是之后执行都会进行执行
        log.debug("worker --- 工作线程进行唤醒机制");
        selector.wakeup();
    }

    @Override
    public void run() {
        while (true) {
            try {
                log.debug("worker --- 工作线程子线程开始进行调用");
                Opt.ofEmptyAble(queue).ifPresent(runnable -> runnable.poll().run());
                // 多线程环境下，可能他先执行，会造成卡死
                log.debug("worker --- 工作线程子线程监听器开始监听");
                selector.select();
                log.debug("worker --- 工作线程子线程监听器成功监听");
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        socketChannel.read(allocate);
                        allocate.flip();
                        System.out.println(StandardCharsets.UTF_8.decode(allocate).toString());
                        allocate.clear();
                        log.debug("worker --- 工作线程子线程监听器都完成");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
