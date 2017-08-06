package com.newbig.app.face2face.gate;

import com.newbig.app.face2face.gate.handler.GateAuthConnectionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protobuf.code.PacketDecoder;
import protobuf.code.PacketEncoder;


public class GateAuthConnection {
    public static void startGateAuthConnection(String ip, int port) {
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
                    pipeline.addLast("GateAuthConnectionHandler", new GateAuthConnectionHandler());  //Auth -> gate
                }
            });

        bootstrap.connect(ip, port);
    }
}
