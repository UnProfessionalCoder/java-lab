package com.newbig.app.binlog;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class TableTool {


    public static final Logger LOGGER = LoggerFactory.getLogger(TableTool.class);
    public static Map<Long, Table> tableInfoMap = Maps.newHashMap();
    public static ResultSet rs = null;

    public static Table getTableInfo(Long tableId) {
        if (tableInfoMap.containsKey(tableId)) {
            return tableInfoMap.get(tableId);
        }
        throw new RuntimeException("出现异常, 没找到tableId");
    }


    public static void setTableInfo(String host, Integer port, String dbName, String username, String password, Long tableId, String tableName) {
        if (tableInfoMap.containsKey(tableId) && !BinlogEventHandler.shouldUpdateTableInfo) {
            return;
        }
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            Map<String, Boolean> primaryKeys = Maps.newHashMap();
            Map<String, String> types = Maps.newHashMap();
            Map<Integer, String> columns = Maps.newHashMap();

            DatabaseMetaData metaData = connection.getMetaData();
            rs = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
            //获取主键
            while (rs.next()) {
                System.out.println("主键-" + rs.getString("COLUMN_NAME"));
                primaryKeys.put(rs.getString("COLUMN_NAME"), Boolean.TRUE);
            }
            ResultSet resultSet = metaData.getColumns(dbName, null, tableName, "%");
            Table table = new Table(dbName, tableName);
            AtomicInteger num = new AtomicInteger(0);
            while (resultSet.next()) {
                String column = resultSet.getString("COLUMN_NAME");
                String type = resultSet.getString("TYPE_NAME");
                types.put(column, type);
                columns.put(num.getAndIncrement(), column);
            }
            table.setColumns(columns);
            table.setTypes(types);
            tableInfoMap.put(tableId, table);
        } catch (Exception e) {
            LOGGER.error("获取connection失败", e);
            throw new RuntimeException(e);
        }
    }


}
