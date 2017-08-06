package com.newbig.app.face2face.auth.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.auth.IMHandler;
import com.newbig.app.face2face.auth.Worker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import protobuf.ParseRegistryMap;
import protobuf.Utils;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.internal.Internal;

@Slf4j
public class CPrivateChatHandler extends IMHandler {

    public CPrivateChatHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        Chat.CPrivateChat msg = (Chat.CPrivateChat) this.msg;
        ByteBuf byteBuf;

        String dest = msg.getDest();
        Long netid = AuthServerHandler.getNetidByUserid(dest);
        if (netid == null) {
            log.error("Dest User not online");
            return;
        }

        Chat.SPrivateChat.Builder sp = Chat.SPrivateChat.newBuilder();
        sp.setContent(msg.getContent());
        byteBuf = Utils.pack2Server(sp.build(), ParseRegistryMap.SPRIVATECHAT, netid, Internal.Dest.Gate, dest);
        ctx.writeAndFlush(byteBuf);

        log.info("message has send from {} to {}", msg.getSelf(), msg.getDest());
    }
}
