package com.newbig.app.face2face.gate.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.gate.utils.ClientConnectionMap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.Utils;
import protobuf.generate.cli2srv.chat.Chat;

@Slf4j
public class GateServerHandler extends SimpleChannelInboundHandler<Message> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //保存客户端连接
        ClientConnectionMap.addClientConnection(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message message) throws Exception {
//        ClientConnection conn = ClientConnectionMap.getClientConnection(channelHandlerContext);
//        ClientMessage.processTransferHandler(message, conn);
        //TODO 最好加一个通知客户端收到消息的通知
        log.info(message.toString());
        Chat.CPrivateChat.Builder cp = Chat.CPrivateChat.newBuilder();
        cp.setContent("11111");
        cp.setSelf("11");
        cp.setDest("12");

        ByteBuf byteBuf = Utils.pack2Client(cp.build());
        channelHandlerContext.writeAndFlush(byteBuf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ClientConnectionMap.removeClientConnection(ctx);
    }
}
