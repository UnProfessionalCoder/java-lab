package com.newbig.app.face2face.gate.handler;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.internal.Internal;

@Slf4j
public class GateLogicConnectionHandler extends SimpleChannelInboundHandler<Message> {
    private static ChannelHandlerContext gateLogicConnection;

    public static ChannelHandlerContext getGatelogicConnection() {
        return gateLogicConnection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        gateLogicConnection = ctx;
        log.info("[Gate-Logic] connection is established");

        //向logic发送Greet
        sendGreet2Logic();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {

    }

    private void sendGreet2Logic() {
        Internal.Greet.Builder ig = Internal.Greet.newBuilder();
        ig.setFrom(Internal.Greet.From.Gate);
        ByteBuf out = Utils.pack2Server(ig.build(), ParseRegistryMap.GREET, -1, Internal.Dest.Logic, "admin");
        getGatelogicConnection().writeAndFlush(out);
        log.info("Gate send Green to Logic.");
    }
}
