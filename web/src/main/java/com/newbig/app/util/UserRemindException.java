package com.newbig.app.util;

/**
 * Created by xiaofan on 17-6-10.
 */
public class UserRemindException extends RuntimeException {

    public UserRemindException() {
        super();
    }

    public UserRemindException(String message) {
        super(message);
    }

    public UserRemindException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRemindException(Throwable cause) {
        super(cause);
    }

}
