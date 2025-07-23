package com.liteGrass.learnNetty.baseNetty.baseNIO;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: ServerSocketLearn
 * @Author: liteGrass
 * @Date: 2025/7/9 18:01
 * @Description: 网络io学习
 */
public class ServerSocketLearn {


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/9 18:03
    * @Desc: 服务端
    */
    @Test
    void testMethodServerV1() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定地址机器端口号
        serverSocketChannel.bind(new InetSocketAddress(8080));
        List<SocketChannel> acceptSocketChannelList = ListUtil.list(false);
        ByteBuffer buffer = ByteBuffer.allocate(20);
        // 开始接受服务段传输数据
        while (true) {
            System.out.println("开始进行数据工作，等待客户端进行链接--------------------");
            SocketChannel socketChannel = serverSocketChannel.accept();
            acceptSocketChannelList.add(socketChannel);
            // 获取到连接的socket
            System.out.println("获取到客户端链接："+ socketChannel);
            // 收集客户端消息信息
            for (SocketChannel channel : acceptSocketChannelList) {
                System.out.println("开始进行数据传输工作");
                channel.read(buffer);
                buffer.flip();
                System.out.println("获取到相应消息的信息：" + StandardCharsets.UTF_8.decode(buffer).toString());
                buffer.clear();
                System.out.println("通道关闭--------------");
            }
        }
    }

    /**
    * @Auther: liteGrass
    * @Date: 2025/7/9 18:01
    * @Desc: 简单版本io学习----客户端
    */
    @Test
    void testMethodClientV1() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress( 8080));
        System.out.println("--------------------------------------");
    }


    //------------------------------------------------------------------
    // 进行改进，V1版本会造成两个方面的阻塞
    // 1、连接阻塞：accept阻塞
    // 2、读取IO阻塞：read阻塞
    /**
    * @Auther: liteGrass
    * @Date: 2025/7/9 19:07
    * @Desc: V2版本
    */
    @Test
    void testMethodV2() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 绑定地址机器端口号
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 关闭连接阻塞
        serverSocketChannel.configureBlocking(false);
        List<SocketChannel> acceptSocketChannelList = ListUtil.list(false);
        ByteBuffer buffer = ByteBuffer.allocate(20);
        // 开始接受服务段传输数据
        while (true) {
            System.out.println("开始进行数据工作，等待客户端进行链接--------------------");
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (ObjectUtil.isNotNull(socketChannel)) {
                // 关闭读取IO阻塞
                socketChannel.configureBlocking(false);
                acceptSocketChannelList.add(socketChannel);
                // 获取到连接的socket
                System.out.println("获取到客户端链接："+ socketChannel);
            }
            // 收集客户端消息信息
            for (SocketChannel channel : acceptSocketChannelList) {
                int read = channel.read(buffer);
                if (read > 0) {
                    System.out.println("开始进行数据传输工作");
                    buffer.flip();
                    System.out.println("获取到相应消息的信息：" + StandardCharsets.UTF_8.decode(buffer).toString());
                    buffer.clear();
                    System.out.println("通道关闭--------------");
                }
            }
            // 睡眠1s
            ThreadUtil.sleep(1000);
        }
    }

    //------------------------------------------------------------------
    // 进行改进，虽然阻塞已经解决，V2版本一直在循环中进行，我们需要有一个监控selector
    // Selector来监控我们的accept以及read、write
    // 如果这有这几个动作，我们才进行相应的代码执行
    /**
    * @Auther: liteGrass
    * @Date: 2025/7/9 20:40
    * @Desc: 监控版本的阻塞
    */
    @Test
    void testMethod() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        // ssc注册到selector的一个keys的set属性中去
        SelectionKey register = serverSocketChannel.register(selector, 0);
        // 监控accept事件
        register.interestOps(SelectionKey.OP_ACCEPT);

        while (true) {
            // 这里阻塞监听
            int select = selector.select();
            System.out.println("监听到相应的时间：" + select);
            // selectedKeys：已经监听到发生动作的socket
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    ssc.accept();
                    System.out.println(StrUtil.format("监听到连接事件：{}，开始建立连接",ssc));
                }
                else if (key.isReadable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(5);
                    sc.read(buffer);
                }
            }
        }
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/10 23:51
    * @Desc: V3版本，
     * 1、解决重复监听问题
     * 2、以及当发送数据为空或者客户端异常断开后一直监听到数据得问题
    */
    @Test
    void testMethodV3() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        // ssc注册到selector的一个keys的set属性中去
        SelectionKey register = serverSocketChannel.register(selector, 0);
        // 监控accept事件
        register.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            // 这里阻塞监听
            int select = selector.select();
            System.out.println("监听到相应的时间：" + select);
            // selectedKeys：已经监听到发生动作的socket
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 解决重复监听得问题
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    System.out.println(StrUtil.format("监听到连接事件：{}，开始建立连接",ssc));
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 注册成为度操作
                    sc.register(selector, SelectionKey.OP_READ);
                }
                else if (key.isReadable()) {
                    // 当一个客户端有异常得时候，不能影响其他客户端得读写
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 当我们得缓冲区过小，但是发送得数据大于缓冲区大小得时候，这个时候会监控到多次select
                        ByteBuffer buffer = ByteBuffer.allocate(20);
                        int read = sc.read(buffer);
                        // 客户端断开连接得是否回向服务端写入-1，我们这把无用得消息进行cancel，防止频繁进行select
                        if (read == -1) {
                            key.cancel();
                        } else {
                            buffer.flip();
                            System.out.println(StandardCharsets.UTF_8.decode(buffer).toString());
                            buffer.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/13 22:15
    * @Desc: V4：解决我们进行拆包的问题
    */
    @Test
    void testMethodV4() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 监听器
        Selector selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, 0);
        register.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            System.out.println("监听到相应的事件");
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 进行移除操作
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    System.out.println(StrUtil.format("监听到连接事件：{}，开始建立连接",ssc));
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 监听读事件，并绑定相应的缓冲区，进行重复读取工作
                    sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(20));
                }
                else if (key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 进行写入buffer
                        int read = sc.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            buffer.flip();
                            for (int i = 0; i < buffer.limit(); i++) {
                                if (buffer.get(i) == '\n') {
                                    // 开始进行读取工作，从position开始读取，读取到i
                                    ByteBuffer target = ByteBuffer.allocate(20);
                                    for (int j = buffer.position(); j < i; j++) {
                                        target.put(buffer.get());
                                    }
                                    // 读取完成后继续向后移动一个位置,跳过\n分隔符
                                    buffer.get();
                                    target.flip();
                                    System.out.println(StandardCharsets.UTF_8.decode(target).toString());
                                }
                            }
                            // 把剩下的分给下次
                            buffer.compact();
                        }
                    } catch (Exception e){
                        key.cancel();
                    }
                }
            }
        }
    }


    /**
     * 缓冲区过小
     * @throws IOException
     */
    @Test
    void testMethodV5() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 监听器
        Selector selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, 0);
        register.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            System.out.println("监听到相应的事件");
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 进行移除操作
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    System.out.println(StrUtil.format("监听到连接事件：{}，开始建立连接",ssc));
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 监听读事件，并绑定相应的缓冲区，进行重复读取工作
                    sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(5));
                }
                else if (key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 进行写入buffer
                        int read = sc.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            buffer.flip();
                            for (int i = 0; i < buffer.limit(); i++) {
                                if (buffer.get(i) == '\n') {
                                    // 开始进行读取工作，从position开始读取，读取到i
                                    ByteBuffer target = ByteBuffer.allocate(i - buffer.position());
                                    for (int j = buffer.position(); j < i; j++) {
                                        target.put(buffer.get());
                                    }
                                    // 读取完成后继续向后移动一个位置,跳过\n分隔符
                                    buffer.get();
                                    target.flip();
                                    System.out.println(StandardCharsets.UTF_8.decode(target).toString());
                                }
                            }
                            // 把剩下的分给下次，如果第一次全部放到缓冲区不够占满，下次在进行读取一直读取到0
                            buffer.compact();
                            // 如果此时buffer == limit进行二倍扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (Exception e){
                        key.cancel();
                    }
                }
            }
        }
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/14 22:21
    * @Desc: 客户端
    */
    @Test
    void testMethodClientV2() throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(8080));

        // 开始读取数据
        int read = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            read += sc.read(buffer);
            System.out.println("read:" + read);
            buffer.clear();
        }
    }

    /**
    * @Auther: liteGrass
    * @Date: 2025/7/14 22:17
    * @Desc: 写入数据
    */
    @Test
    void testMethodV6() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 监听器
        Selector selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, 0);
        register.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            System.out.println("监听到相应的事件");
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 进行移除操作
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    System.out.println(StrUtil.format("监听到连接事件：{}，开始建立连接",ssc));
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 监听读事件，并绑定相应的缓冲区，进行重复读取工作
                    sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(5));
                    // 向客户端写入数据
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 20000000; i++) {
                        sb.append("s");
                    }
                    ByteBuffer writeBuffer = StandardCharsets.UTF_8.encode(sb.toString());
                    // 会造成发送很多0数据，造成线程占用，我们可以进行相应得处理，监听写入数据
                    while (writeBuffer.hasRemaining()) {
                        int write = sc.write(writeBuffer);
                        System.out.println("write:" + write);
                    }
                }
                else if (key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 进行写入buffer
                        int read = sc.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            buffer.flip();
                            for (int i = 0; i < buffer.limit(); i++) {
                                if (buffer.get(i) == '\n') {
                                    // 开始进行读取工作，从position开始读取，读取到i
                                    ByteBuffer target = ByteBuffer.allocate(i - buffer.position());
                                    for (int j = buffer.position(); j < i; j++) {
                                        target.put(buffer.get());
                                    }
                                    // 读取完成后继续向后移动一个位置,跳过\n分隔符
                                    buffer.get();
                                    target.flip();
                                    System.out.println(StandardCharsets.UTF_8.decode(target).toString());
                                }
                            }
                            // 把剩下的分给下次，如果第一次全部放到缓冲区不够占满，下次在进行读取一直读取到0
                            buffer.compact();
                            // 如果此时buffer == limit进行二倍扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (Exception e){
                        key.cancel();
                    }
                }
            }
        }
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/7/14 22:39
    * @Desc: V7: 监控写入
    */
    @Test
    void testMethodV7() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        // 设置非阻塞
        serverSocketChannel.configureBlocking(false);
        // 监听器
        Selector selector = Selector.open();
        SelectionKey register = serverSocketChannel.register(selector, 0);
        register.interestOps(SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            System.out.println("监听到相应的事件");
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 进行移除操作
                iterator.remove();
                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    System.out.println(StrUtil.format("监听到连接事件：{}，开始建立连接",ssc));
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    // 监听读事件，并绑定相应的缓冲区，进行重复读取工作
                    sc.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(5));
                    // 向客户端写入数据
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 20000000; i++) {
                        sb.append("s");
                    }
                    ByteBuffer writeBuffer = StandardCharsets.UTF_8.encode(sb.toString());
                    // 会造成发送很多0数据，造成线程占用，我们可以进行相应得处理，监听写入数据
                    // 先进性一次写入，判断是否写入完成，如果还有剩余得数据，监控写操作
                    int write = sc.write(writeBuffer);
                    System.out.println("write:" + write);

                    if (writeBuffer.hasRemaining()) {
                        key.interestOps(key.interestOps() + SelectionKey.OP_WRITE);
                        // 保存之前剩余没有写入得数据
                        key.attach(writeBuffer);
                    }
                }
                else if (key.isWritable()) {
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    // 开始进行数据写入
                    int write = sc.write(buffer);
                    System.out.println("write:" + write);
                    // 判断是否写入完成
                    if (!buffer.hasRemaining()) {
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                        key.attach(ByteBuffer.allocate(5));
                        buffer.clear();
                    }
                }
                else if (key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 进行写入buffer
                        int read = sc.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
                            buffer.flip();
                            for (int i = 0; i < buffer.limit(); i++) {
                                if (buffer.get(i) == '\n') {
                                    // 开始进行读取工作，从position开始读取，读取到i
                                    ByteBuffer target = ByteBuffer.allocate(i - buffer.position());
                                    for (int j = buffer.position(); j < i; j++) {
                                        target.put(buffer.get());
                                    }
                                    // 读取完成后继续向后移动一个位置,跳过\n分隔符
                                    buffer.get();
                                    target.flip();
                                    System.out.println(StandardCharsets.UTF_8.decode(target).toString());
                                }
                            }
                            // 把剩下的分给下次，如果第一次全部放到缓冲区不够占满，下次在进行读取一直读取到0
                            buffer.compact();
                            // 如果此时buffer == limit进行二倍扩容
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (Exception e){
                        key.cancel();
                    }
                }
            }
        }
    }

}
