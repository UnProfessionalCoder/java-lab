package com.newbig.app.binlog;

import java.util.Map;

public class Table {
    private String dbName;
    private String tableName;
    /**
     * columnId : coumn Name
     */
    private Map<Integer, String> columns;
    /**
     * 每列 类型
     */
    private Map<String, String> types;
    /**
     * 是否为主键
     * key： column name
     * value：true false
     */
    private Map<String, Boolean> isPrimaryKey;

    public Table(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Map<Integer, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<Integer, String> columns) {
        this.columns = columns;
    }

    public Map<String, Boolean> getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Map<String, Boolean> isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }
}
