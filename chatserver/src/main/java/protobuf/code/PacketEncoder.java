package protobuf.code;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import protobuf.analysis.ParseMap;

@Slf4j
public class PacketEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out)
            throws Exception {

        byte[] bytes = msg.toByteArray();// 将对象转换为byte
        int ptoNum = ParseMap.msg2ptoNum.get(msg);
        int length = bytes.length;

        /* 加密消息体
        ThreeDES des = ctx.channel().attr(ClientAttr.ENCRYPT).get();
        byte[] encryptByte = des.encrypt(bytes);
        int length = encryptByte.length;*/

        ByteBuf buf = Unpooled.buffer(8 + length);
        buf.writeInt(length);
        buf.writeInt(ptoNum);
        buf.writeBytes(bytes);
        out.writeBytes(buf);

        log.info("GateServer Send Message, remoteAddress: {}, content length {}, ptoNum: {}", ctx.channel().remoteAddress(), length, ptoNum);

    }
}
