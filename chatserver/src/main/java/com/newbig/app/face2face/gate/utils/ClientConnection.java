package com.newbig.app.face2face.gate.utils;

import com.newbig.app.face2face.thirdparty.threedes.ThreeDES;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端连接的封装类
 */
public class ClientConnection {

    private static final AtomicLong netidGenerator = new AtomicLong(0);
    public static AttributeKey<ThreeDES> ENCRYPT = AttributeKey.valueOf("encrypt");
    public static AttributeKey<Long> NETID = AttributeKey.valueOf("netid");

    private String userId;
    private long netId;
    private ChannelHandlerContext ctx;

    ClientConnection(ChannelHandlerContext c) {
        netId = netidGenerator.incrementAndGet();
        ctx = c;
        ctx.attr(ClientConnection.NETID).set(netId);
    }

    public long getNetId() {
        return netId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void readUserIdFromDB() {

    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }
}
