package com.newbig.app.face2face.auth;


import com.google.protobuf.Message;
import com.newbig.app.face2face.auth.handler.CLoginHandler;
import com.newbig.app.face2face.auth.handler.CPrivateChatHandler;
import com.newbig.app.face2face.auth.handler.CRegisterHandler;
import com.newbig.app.face2face.auth.handler.GreetHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import protobuf.analysis.ParseMap;
import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;
import protobuf.generate.internal.Internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HandlerManager {
    private static final Map<Integer, Constructor<? extends IMHandler>> handlers = new HashMap<>();

    public static void register(Class<? extends Message> msg, Class<? extends IMHandler> handler) {
        int num = ParseMap.getPtoNum(msg);
        try {
            Constructor<? extends IMHandler> constructor = handler.getConstructor(String.class, long.class, Message.class, ChannelHandlerContext.class);
            handlers.put(num, constructor);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static IMHandler getHandler(int msgNum, String userId, long netId,
                                       Message msg, ChannelHandlerContext ctx)
        throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<? extends IMHandler> constructor = handlers.get(msgNum);
        if (constructor == null) {
            log.error("handler not exist, Message Number: {}", msgNum);
            return null;
        }

        return constructor.newInstance(userId, netId, msg, ctx);
    }

    public static void initHandlers() {
        HandlerManager.register(Internal.Greet.class, GreetHandler.class);
        HandlerManager.register(Auth.CLogin.class, CLoginHandler.class);
        HandlerManager.register(Auth.CRegister.class, CRegisterHandler.class);
        HandlerManager.register(Chat.CPrivateChat.class, CPrivateChatHandler.class);

    }
}
