package com.newbig.app.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;

/**
 * Created by xiaofan on 17-6-3.
 */
public class BinlogTest {

    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        int port = 3306;
        String userName = "root";
        String password = "123456";
        try {
            BinaryLogClient client = new BinaryLogClient(hostName, port, userName, password);
            client.setBinlogPosition(1611);
            client.setBinlogFilename("mysql-bin.000034");
            client.registerEventListener(event -> BinlogEventHandler.handle(event, hostName, port, userName, password));
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
