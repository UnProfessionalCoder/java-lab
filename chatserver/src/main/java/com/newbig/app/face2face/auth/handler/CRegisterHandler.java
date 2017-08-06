package com.newbig.app.face2face.auth.handler;

import com.google.protobuf.Message;
import com.newbig.app.face2face.auth.IMHandler;
import com.newbig.app.face2face.auth.Worker;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import protobuf.generate.cli2srv.login.Auth;

@Slf4j
public class CRegisterHandler extends IMHandler {

    public CRegisterHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        Auth.CRegister msg = (Auth.CRegister) this.msg;

//        String userid = msg.getUserid();
//        String passwd = msg.getPasswd();
//        Account account = new Account();
//        account.setUserid(userid);
//        account.setPasswd(passwd);
//
//        //todo 写数据库要加锁
//
//        if (jedis.exists(UserUtils.genDBKey(userid))) {
//            RouteUtil.sendResponse(Common.ACCOUNT_DUMPLICATED, "Account already exists", netid, userid);
//            logger.info("Account already exists, userid: {}", userid);
//            return;
//        } else {
//            jedis.hset(UserUtils.genDBKey(userid), UserUtils.userFileds.Account.field, DBOperator.Serialize(account));
//            RouteUtil.sendResponse(Common.REGISTER_OK, "User registerd successd",netid, userid);
//            logger.info("User registerd successd, userid: {}", userid);
//        }

    }

}


