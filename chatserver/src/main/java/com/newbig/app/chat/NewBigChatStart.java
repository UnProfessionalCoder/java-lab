package com.newbig.app.chat;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NewBigChatStart {

    public static void start(Integer websocketPort) {
        final NewBigChatServer server = new NewBigChatServer(websocketPort);
        server.init();
        server.start();
        // 注册进程钩子，在JVM进程关闭前释放资源
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.shutdown();
                log.warn(">>>>>>>>>> jvm shutdown");
                System.exit(0);
            }
        });
    }
    public static void main(String[] args) {
        start(10000);
    }
}
