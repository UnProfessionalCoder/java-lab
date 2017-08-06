package com.newbig.app.face2face.logic.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.logic.IMHandler;
import com.newbig.app.face2face.logic.Worker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import protobuf.generate.internal.Internal;

@Slf4j
public class GreetHandler extends IMHandler {

    public GreetHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        Internal.Greet msg = (Internal.Greet) this.msg;
        Internal.Greet.From from = msg.getFrom();

        if (from == Internal.Greet.From.Auth) {
            LogicServerHandler.setAuthLogicConnection(ctx);
            log.info("[Auth-Logic] connection is established");
        } else if (from == Internal.Greet.From.Gate) {
            LogicServerHandler.setGateLogicConnection(ctx);
            log.info("[Gate-Logic] connection is established");

        }
    }
}
