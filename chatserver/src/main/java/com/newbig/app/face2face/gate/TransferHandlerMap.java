package com.newbig.app.face2face.gate;

import protobuf.generate.cli2srv.chat.Chat;
import protobuf.generate.cli2srv.login.Auth;

import java.io.IOException;

public class TransferHandlerMap {
    public static void initRegistry() throws IOException {
        ClientMessage.registerTranferHandler(1000, ClientMessage::transfer2Auth, Auth.CLogin.class);
        ClientMessage.registerTranferHandler(1001, ClientMessage::transfer2Auth, Auth.CRegister.class);
        ClientMessage.registerTranferHandler(1003, ClientMessage::transfer2Logic, Chat.CPrivateChat.class);
    }
}
