package com.newbig.app.face2face.auth;

import com.newbig.app.face2face.auth.handler.AuthLogicConnectionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protobuf.code.PacketDecoder;
import protobuf.code.PacketEncoder;


public class AuthLogicConnection {

    public static void startAuthLogicConnection(String ip, int port) {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap()
            .group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel)
                    throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast("MessageDecoder", new PacketDecoder());
                    pipeline.addLast("MessageEncoder", new PacketEncoder());
                    pipeline.addLast("AuthLogicConnectionHandler", new AuthLogicConnectionHandler());  //Auth -> gate
                }
            });

        bootstrap.connect(ip, port);
    }
}
