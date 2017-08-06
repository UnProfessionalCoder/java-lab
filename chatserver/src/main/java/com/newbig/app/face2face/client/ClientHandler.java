package com.newbig.app.face2face.client;

import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import protobuf.Utils;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 模拟客户端聊天：自己给自己发消息
 */
@Slf4j
public class ClientHandler extends SimpleChannelInboundHandler<Message> {
    public static ChannelHandlerContext gateClientConnection;
    public static AtomicLong increased = new AtomicLong(1);
    private static int count = 0;
    String userId = "";
    boolean verify = true;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
        gateClientConnection = ctx;
        String passwd = "123";
        userId = Long.toString(increased.getAndIncrement());

        sendCRegister(ctx, userId, passwd);
        sendCLogin(ctx, userId, passwd);
        sendMessage();
    }

    void sendCRegister(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CRegister.Builder cb = Auth.CRegister.newBuilder();
        cb.setUserid(userid);
        cb.setPasswd(passwd);

        ByteBuf byteBuf = Utils.pack2Client(cb.build());
        ctx.writeAndFlush(byteBuf);
        log.info("send CRegister userid:{}", userId);
    }

    void sendCLogin(ChannelHandlerContext ctx, String userid, String passwd) {
        Auth.CLogin.Builder loginInfo = Auth.CLogin.newBuilder();
        loginInfo.setUserid(userid);
        loginInfo.setPasswd(passwd);
        loginInfo.setPlatform("ios");
        loginInfo.setAppVersion("1.0.0");

        ByteBuf byteBuf = Utils.pack2Client(loginInfo.build());
        ctx.writeAndFlush(byteBuf);
        log.info("send CLogin userid:{}", userId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Message msg) throws Exception {
        log.info("received message: {}", msg.toString());
        if (msg instanceof Auth.SResponse) {
            Auth.SResponse sp = (Auth.SResponse) msg;
            int code = sp.getCode();
            String desc = sp.getDesc();
            switch (code) {
                //登录成功
                case Common.VERYFY_PASSED:
                    log.info("Login succeed, description: {}", desc);
                    verify = true;
                    break;
                //登录账号不存在
                case Common.ACCOUNT_INEXIST:
                    log.info("Account inexsit, description: {}", desc);
                    break;
                //登录账号或密码错误
                case Common.VERYFY_ERROR:
                    log.info("Account or passwd Error, description: {}", desc);
                    break;
                //账号已被注册
                case Common.ACCOUNT_DUMPLICATED:
                    log.info("Dumplicated registry, description: {}", desc);
                    break;
                //注册成功
                case Common.REGISTER_OK:
                    log.info("User registerd successd, description: {}", desc);
                    break;
                case Common.Msg_SendSuccess:
                    log.info("Chat Message Send Successed, description: {}", desc);
                default:
                    log.info("Unknow code: {}", code);
            }
        } else if (msg instanceof Chat.SPrivateChat) {
            log.info("{} receiced chat message: {}.Total:{}", userId, ((Chat.SPrivateChat) msg).getContent(), ++count);
        }

        //这样设置的原因是，防止两方都阻塞在输入上
        if (verify) {
            sendMessage();
            Thread.sleep(Client.frequency);
        }
    }

    void sendMessage() {
//        log.info("WelCome To Face2face Chat Room, You Can Say Something Now: ");
//        Scanner sc = new Scanner(System.in);
//        String content = sc.nextLine();
        String content = "Hello, I am Tom!";
//        log.info("{} Send Message: {} to {}", userId, content, _friend);

        Chat.CPrivateChat.Builder cp = Chat.CPrivateChat.newBuilder();
        cp.setContent(content);
        cp.setSelf(userId);
        cp.setDest(userId);

        ByteBuf byteBuf = Utils.pack2Client(cp.build());
        gateClientConnection.writeAndFlush(byteBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
