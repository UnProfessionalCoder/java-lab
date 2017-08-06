package com.newbig.app.face2face.auth;

import com.newbig.app.face2face.auth.starter.AuthStarter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Worker extends Thread {
    public static Worker[] workers;
    private final BlockingQueue<IMHandler> tasks = new LinkedBlockingDeque<>();
    public volatile boolean stop = false;

    public static void dispatch(String userId, IMHandler handler) {
        int workId = getWorkId(userId);
        if (handler == null) {
            log.error("handler is null");
            return;
        }
        workers[workId].tasks.offer(handler);
    }

    public static int getWorkId(String str) {
        return str.hashCode() % AuthStarter.workNum;
    }

    public static void startWorker(int workNum) {
        workers = new Worker[workNum];
        for (int i = 0; i < workNum; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public static void stopWorkers() {
        for (int i = 0; i < AuthStarter.workNum; i++) {
            workers[i].stop = true;
        }
    }

    @Override
    public void run() {
        while (!stop) {
            IMHandler handler = null;
            try {
                handler = tasks.poll(600, TimeUnit.MILLISECONDS);
                if (handler == null)
                    continue;
            } catch (InterruptedException e) {
                log.error("Caught Exception");
            }
            try {
                assert handler != null;
                handler.jedis = AuthStarter.redisPoolManager.getJedis();
                handler.excute(this);
            } catch (Exception e) {
                log.error("Caught Exception");
            } finally {
                AuthStarter.redisPoolManager.releaseJedis(handler.jedis);
                handler.jedis = null;
            }
        }
    }
}
