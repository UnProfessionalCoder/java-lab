package com.newbig.app.face2face.logic;

import com.newbig.app.face2face.logic.starter.LogicStarter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Worker extends Thread {
    public static Worker[] _workers;
    private final BlockingQueue<IMHandler> _tasks = new LinkedBlockingDeque<>();
    public volatile boolean _stop = false;

    public static void dispatch(String userId, IMHandler handler) {
        int workId = getWorkId(userId);
        if (handler == null)
            log.error("handler is null");

        _workers[workId]._tasks.offer(handler);
    }

    public static int getWorkId(String str) {
        return str.hashCode() % LogicStarter.workNum;
    }

    public static void startWorker(int workNum) {
        _workers = new Worker[workNum];
        for (int i = 0; i < workNum; i++) {
            _workers[i] = new Worker();
            _workers[i].start();
        }
    }

    public static void stopWorkers() {
        for (int i = 0; i < LogicStarter.workNum; i++) {
            _workers[i]._stop = true;
        }
    }

    @Override
    public void run() {
        while (!_stop) {
            IMHandler handler = null;
            try {
                handler = _tasks.poll(600, TimeUnit.MILLISECONDS);
                if (handler == null)
                    continue;
            } catch (InterruptedException e) {
                log.error("Caught Exception");
            }
            try {
                assert handler != null;
                handler.excute(this);
            } catch (Exception e) {
                log.error("Caught Exception");
            }
        }
    }
}
