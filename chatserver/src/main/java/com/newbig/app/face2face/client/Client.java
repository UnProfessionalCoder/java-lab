package com.newbig.app.face2face.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import protobuf.ParseRegistryMap;
import protobuf.code.PacketDecoder;
import protobuf.code.PacketEncoder;

@Slf4j
public class Client {
    public static final int clientNum = Integer.parseInt(System.getProperty("size", "10"));
    public static final int frequency = 100;  //ms
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "9090"));

    public static void main(String[] args) throws Exception {
        beginPressTest();
    }

    public static void beginPressTest() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline p = ch.pipeline();

                    p.addLast("MessageDecoder", new PacketDecoder());
                    p.addLast("MessageEncoder", new PacketEncoder());
                    p.addLast(new ClientHandler());
                }
            });

        // Start the client.
        for (int i = 1; i <= clientNum; i++) {
            startConnection(b, i);
        }

    }

    private static void startConnection(Bootstrap b, int index) {
        b.connect(HOST, PORT)
            .addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future)
                    throws Exception {
                    if (future.isSuccess()) {
                        //init registry
                        ParseRegistryMap.initRegistry();
                        log.info("Client[{}] connected Gate Successed...", index);
                    } else {
                        log.error("Client[{}] connected Gate Failed", index);
                    }
                }
            });
    }
}

