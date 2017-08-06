package protobuf.code;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import protobuf.analysis.ParseMap;

import java.util.List;

@Slf4j
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
                          List<Object> out) throws Exception {

        in.markReaderIndex();

        if (in.readableBytes() < 4) {
            log.info("readableBytes length less than 4 bytes, ignored");
            in.resetReaderIndex();
            return;
        }

        int length = in.readInt();

        if (length < 0) {
            ctx.close();
            log.error("message length less than 0, channel closed");
            return;
        }

        if (length > in.readableBytes() - 4) {
            //注意！编解码器加这种in.readInt()日志，在大并发的情况下很可能会抛数组越界异常！
            //log.error("message received is incomplete,ptoNum:{}, length:{}, readable:{}", in.readInt(), length, in.readableBytes());
            in.resetReaderIndex();
            return;
        }

        int ptoNum = in.readInt();


        ByteBuf byteBuf = Unpooled.buffer(length);

        in.readBytes(byteBuf);

        try {
            /* 解密消息体
            ThreeDES des = ctx.channel().attr(ClientAttr.ENCRYPT).get();
            byte[] bareByte = des.decrypt(inByte);*/

            byte[] body= byteBuf.array();

            Message msg = ParseMap.getMessage(ptoNum, body);
            out.add(msg);
            log.info("GateServer Received Message: content length {}, ptoNum: {}", length, ptoNum);

        } catch (Exception e) {
            log.error(ctx.channel().remoteAddress() + ",decode failed.", e);
        }
    }

}
