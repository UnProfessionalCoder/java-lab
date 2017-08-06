package com.newbig.app.face2face.auth.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.auth.IMHandler;
import com.newbig.app.face2face.auth.Worker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GreetHandler extends IMHandler {

    public GreetHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        AuthServerHandler.setGateAuthConnection(ctx);
        log.info("[Gate-Auth] connection is established");
    }
}
