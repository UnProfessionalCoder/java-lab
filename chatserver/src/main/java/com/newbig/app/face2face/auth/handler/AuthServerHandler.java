package com.newbig.app.face2face.auth.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.auth.HandlerManager;
import com.newbig.app.face2face.auth.IMHandler;
import com.newbig.app.face2face.auth.Worker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.analysis.ParseMap;
import protobuf.generate.internal.Internal;

import java.util.HashMap;

@Slf4j
public class AuthServerHandler extends SimpleChannelInboundHandler<Message> {
    private static ChannelHandlerContext gateAuthConnection;
    private static HashMap<String, Long> userid2netidMap = new HashMap<>();

    public static ChannelHandlerContext getGateAuthConnection() {
        if (gateAuthConnection != null) {
            return gateAuthConnection;
        } else {
            return null;
        }
    }

    public static void setGateAuthConnection(ChannelHandlerContext ctx) {
        gateAuthConnection = ctx;
    }

    public static void putInUseridMap(String userid, Long netId) {
        userid2netidMap.put(userid, netId);
    }

    public static Long getNetidByUserid(String userid) {
        Long netid = userid2netidMap.get(userid);
        if (netid != null) {
            return netid;
        } else {
            return null;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
        Internal.GTransfer gt = (Internal.GTransfer) message;
        int ptoNum = gt.getPtoNum();
        Message msg = ParseMap.getMessage(ptoNum, gt.getMsg().toByteArray());

        IMHandler handler;
        if (msg instanceof Internal.Greet) {
            //来自gate的连接请求
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), gt.getNetId(), msg, channelHandlerContext);
        } else {
            handler = HandlerManager.getHandler(ptoNum, gt.getUserId(), gt.getNetId(), msg, getGateAuthConnection());
        }

        Worker.dispatch(gt.getUserId(), handler);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        throws Exception {
        // super.exceptionCaught(ctx, cause);
        log.error("An Exception Caught");
    }
}
