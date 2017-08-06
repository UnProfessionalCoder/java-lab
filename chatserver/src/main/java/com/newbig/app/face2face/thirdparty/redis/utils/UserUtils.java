package com.newbig.app.face2face.thirdparty.redis.utils;

import java.nio.charset.Charset;

/**
 * Created by Dell on 2016/2/22.
 */
public class UserUtils {
    private static Charset charset = Charset.forName("UTF-8");

    public static String genDBKey(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("userId#").append(userId);
        return sb.toString();
    }

    public static enum userFileds {
        Account;

        public final byte[] field;

        private userFileds() {
            this.field = this.name().toLowerCase().getBytes(Charset.forName("UTF-8"));
        }
    }
}
