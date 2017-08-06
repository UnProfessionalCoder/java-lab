package com.newbig.app.face2face.logic.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.logic.HandlerManager;
import com.newbig.app.face2face.logic.IMHandler;
import com.newbig.app.face2face.logic.Worker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.analysis.ParseMap;
import protobuf.generate.internal.Internal;

@Slf4j
public class LogicServerHandler extends SimpleChannelInboundHandler<Message> {
    private static ChannelHandlerContext gateLogicConnection;
    private static ChannelHandlerContext authLogicConnection;

    public static ChannelHandlerContext getGateLogicConnection() {
        if (gateLogicConnection != null) {
            return gateLogicConnection;
        } else {
            return null;
        }
    }

    public static void setGateLogicConnection(ChannelHandlerContext ctx) {
        gateLogicConnection = ctx;
    }

    public static ChannelHandlerContext getAuthLogicConnection() {
        if (authLogicConnection != null) {
            return authLogicConnection;
        } else {
            return null;
        }
    }

    public static void setAuthLogicConnection(ChannelHandlerContext ctx) {
        authLogicConnection = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gt = (Internal.GTransfer) message;
        int ptoNum = gt.getPtoNum();
        Message msg = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());
        log.info(msg.toString());
        IMHandler handler;
        if (msg instanceof Internal.Greet) {
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), gt.getNetId(), msg, channelHandlerContext);
        } else {
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), gt.getNetId(), msg, getGateLogicConnection());
        }

        Worker.dispatch(gt.getUserId(), handler);

    }
}

