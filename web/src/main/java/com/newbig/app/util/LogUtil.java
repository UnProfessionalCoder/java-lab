package com.newbig.app.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.event.Level;

import java.io.PrintWriter;
import java.io.StringWriter;


public class LogUtil {
    static Logger logger = LoggerFactory.getLogger("bizLogger");

    public static synchronized Logger getLogger(String filename, Level level) {
        MDC.put("logFileName", filename);
        return logger;
    }

    public static void info(String type, Object object) {
        if (Exception.class.isInstance(object)) {
            object = getException((Exception) object);
        }
        Logger _logger = getLogger(type, Level.INFO);
        _logger.info(JSON.toJSONString(object));
    }

    public static void info(String type, String object) {
        Logger _logger = getLogger(type, Level.INFO);
        _logger.info(object);
    }

    public static void info(String type, String message, Object object) {
        if (Exception.class.isInstance(object)) {
            object = getException((Exception) object);
        }
        Logger _logger = getLogger(type, Level.INFO);
        _logger.info(message + ":" + JSON.toJSONString(object));
    }

    public static String getException(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return "message:" + e.getMessage() + ",stacktrace:" + sw.getBuffer();
    }
}
