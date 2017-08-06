package com.newbig.app.face2face.logic;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

public abstract class IMHandler {
    protected final String userid;
    protected final long netid;
    protected final Message msg;
    protected ChannelHandlerContext ctx;

    protected IMHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        this.userid = userid;
        this.netid = netid;
        this.msg = msg;
        this.ctx = ctx;
    }

    protected abstract void excute(Worker worker);
}
