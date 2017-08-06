package com.newbig.app.face2face.auth.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.auth.HandlerManager;
import com.newbig.app.face2face.auth.IMHandler;
import com.newbig.app.face2face.auth.Worker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.analysis.ParseMap;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.internal.Internal;

@Slf4j
public class AuthLogicConnectionHandler extends SimpleChannelInboundHandler<Message> {

    private static ChannelHandlerContext authLogicConnection;

    public static ChannelHandlerContext getAuthLogicConnection() {
        return authLogicConnection;
    }

    public static void setAuthLogicConnecttion(ChannelHandlerContext ctx) {
        authLogicConnection = ctx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        setAuthLogicConnecttion(ctx);
        log.info("[Auth-Logic] connection is established");

        //向logic发送Greet协议
        sendGreet2Logic();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gt = (Internal.GTransfer) message;
        int ptoNum = gt.getPtoNum();
        Message msg = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());

        IMHandler handler = null;
        if (msg instanceof Chat.CPrivateChat) {
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), -1L, msg, AuthServerHandler.getGateAuthConnection());
        } else {
            log.error("Error Messgae Type: {}", msg.getClass());
            return;
        }

        Worker.dispatch(gt.getUserId(), handler);

    }

    private void sendGreet2Logic() {
        Internal.Greet.Builder ig = Internal.Greet.newBuilder();
        ig.setFrom(Internal.Greet.From.Auth);
        ByteBuf out = Utils.pack2Server(ig.build(), ParseRegistryMap.GREET, -1, Internal.Dest.Logic, "admin");
        getAuthLogicConnection().writeAndFlush(out);
        log.info("Auth send Green to Logic.");
    }
}
