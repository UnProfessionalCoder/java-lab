package com.newbig.app.face2face.auth.handler;

import com.google.protobuf.Message;
import com.newbig.app.chat.entity.Account;
import com.newbig.app.face2face.auth.IMHandler;
import com.newbig.app.face2face.auth.Worker;
import com.newbig.app.face2face.auth.utils.Common;
import com.newbig.app.face2face.auth.utils.RouteUtil;
import com.newbig.app.face2face.thirdparty.redis.utils.UserUtils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protobuf.generate.cli2srv.login.Auth;

public class CLoginHandler extends IMHandler {
    private static final Logger logger = LoggerFactory.getLogger(CLoginHandler.class);

    public CLoginHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        super(userid, netid, msg, ctx);
    }

    @Override
    protected void excute(Worker worker) {
        Auth.CLogin msg = (Auth.CLogin) this.msg;
        Account account;

        if (!jedis.exists(UserUtils.genDBKey(userid))) {
            RouteUtil.sendResponse(Common.ACCOUNT_INEXIST, "Account not exists", netid, userid);
            logger.info("Account not exists, userid: {}", userid);
            return;
        } else {
            account = new Account();
            account.setUserid(userid);
            account.setPasswd(jedis.get(UserUtils.genDBKey(userid)));
        }

        if (account.getUserid().equals(userid) && account.getPasswd().equals(msg.getPasswd())) {
            AuthServerHandler.putInUseridMap(userid, netid);
            RouteUtil.sendResponse(Common.VERYFY_PASSED, "Verify passed", netid, userid);
            logger.info("userid: {} verify passed", userid);
        } else {
            RouteUtil.sendResponse(Common.VERYFY_ERROR, "Account not exist or passwd error", netid, userid);
            logger.info("userid: {} verify failed", userid);
            return;
        }
    }
}
