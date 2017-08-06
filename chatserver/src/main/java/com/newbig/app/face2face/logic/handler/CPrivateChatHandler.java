package com.newbig.app.face2face.logic.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.logic.IMHandler;
import com.newbig.app.face2face.logic.Worker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;
import protobuf.generate.internal.Internal;

@Slf4j
public class CPrivateChatHandler extends IMHandler {

    public CPrivateChatHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        Chat.CPrivateChat msg = (Chat.CPrivateChat) this.msg;
        ByteBuf byteBuf = null;

        //转发给auth
        byteBuf = Utils.pack2Server(this.msg, ParseRegistryMap.CPRIVATECHAT, Internal.Dest.Auth, msg.getDest());
        LogicServerHandler.getAuthLogicConnection().writeAndFlush(byteBuf);

        //给发消息的人回应
        Auth.SResponse.Builder sr = Auth.SResponse.newBuilder();
        sr.setCode(300);
        sr.setDesc("Server received message successed");
        byteBuf = Utils.pack2Client(sr.build());
        ctx.writeAndFlush(byteBuf);
    }
}
