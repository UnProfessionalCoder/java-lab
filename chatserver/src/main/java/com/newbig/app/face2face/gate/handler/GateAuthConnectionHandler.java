package com.newbig.app.face2face.gate.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.gate.utils.ClientConnectionMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.analysis.ParseMap;
import protobuf.generate.internal.Internal;

@Slf4j
public class GateAuthConnectionHandler extends SimpleChannelInboundHandler<Message> {
    private static ChannelHandlerContext _gateAuthConnection;

    public static ChannelHandlerContext getGateAuthConnection() {
        return _gateAuthConnection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        _gateAuthConnection = ctx;
        log.info("[Gate-Auth] connection is established");

        sendGreet2Auth();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gtf = (Internal.GTransfer) message;

        Message cmd = ParseMap.getMessage(gtf.getPtoNum(), gtf.getMsg().toByteArray());

        ByteBuf out = Utils.pack2Client(cmd);

        ClientConnectionMap.getClientConnection(gtf.getNetId()).getCtx().writeAndFlush(out);
    }

    private void sendGreet2Auth() {
        //向auth送Greet协议
        Internal.Greet.Builder ig = Internal.Greet.newBuilder();
        ig.setFrom(Internal.Greet.From.Gate);
        ByteBuf out = Utils.pack2Server(ig.build(), ParseRegistryMap.GREET, -1, Internal.Dest.Auth, "admin");
        getGateAuthConnection().writeAndFlush(out);
        log.info("Gate send Green to Auth.");
    }
}
