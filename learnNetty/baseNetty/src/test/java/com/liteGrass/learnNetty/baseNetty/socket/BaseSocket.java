package com.liteGrass.learnNetty.baseNetty.socket;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @ClassName BaseSocket
 * @Author liteGrass
 * @Date 2025/6/11 6:58
 * @Description socket客户端
 *
 * http: 他是应用层的协议
 * tcp/udp: 他是传输层的协议
 *
 *
 */

public class BaseSocket {


    /**
     * @Auther: liteGrass
     * @Date: 2025/6/11 7:11
     * @Desc: 测试客户端代码
     * 他主要是一个客户端程序
     */
    @Test
    void testMethodSocket() throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.write("send date to socket");
    }


    /**
    * @Auther: liteGrass
    * @Date: 2025/6/11 7:14
    * @Desc: 服务端
    */
    @Test
    void testMethodServerSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

    }

}
