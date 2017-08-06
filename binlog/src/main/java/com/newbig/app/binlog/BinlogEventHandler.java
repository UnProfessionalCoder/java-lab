package com.newbig.app.binlog;

import com.alibaba.fastjson.JSON;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.shyiko.mysql.binlog.event.EventType.*;
import static com.github.shyiko.mysql.binlog.event.EventType.ANONYMOUS_GTID;
import static com.github.shyiko.mysql.binlog.event.EventType.QUERY;

/**
 * Created by xiaofan on 17-6-4.
 */
public class BinlogEventHandler {
    //处理是单线程的，不需要考虑并发的情况
    public static Boolean shouldUpdateTableInfo = Boolean.FALSE;
    public static long position=0;
    public static void handle(Event event, String hostName, int port, String userName, String password) {
        EventType eventType = event.getHeader().getEventType();

//        System.out.println("position-"+eventHeaderV4.get());

        switch (eventType) {
            case ROTATE:{
                RotateEventData rotateEventData = (RotateEventData)event.getData();
                System.out.println("position-"+rotateEventData.getBinlogFilename());
                System.out.println("position-"+rotateEventData.getBinlogPosition());
                break;
            }
            case ANONYMOUS_GTID: {
                //1. 这里不作任何处理
                break;
            }
            case QUERY: {
                //2.data.getSql() 在 更改表结构时输出 Alter Table ..SQL语句, 其他时候只返回BEGIN
                //这里获取不到 tableId
                QueryEventData data = event.getData();
//                System.out.println("sql+++"+data.getSql());
                if (data.getSql() == null || !Objects.equals("BEGIN", data.getSql())) {
                    shouldUpdateTableInfo = Boolean.TRUE;
                } else {
                    shouldUpdateTableInfo = Boolean.FALSE;
                }
                break;
            }

            case TABLE_MAP: {
                //3. tableId 和table
                TableMapEventData tableMapEventData = event.getData();
                TableTool.setTableInfo(hostName, port, tableMapEventData.getDatabase(), userName,
                    password, tableMapEventData.getTableId(), tableMapEventData.getTable().toString());
            }
            break;

            case WRITE_ROWS:
            case EXT_WRITE_ROWS: {
                WriteRowsEventData wrtiteRowsEvent = event.getData();
                List<Serializable[]> rows = wrtiteRowsEvent.getRows();
                Table table = TableTool.getTableInfo(wrtiteRowsEvent.getTableId());
                Map<Integer, String> columns = table.getColumns();
                System.out.println("===========write=============");
                for (Serializable[] row : rows) {
                    Map<String, Object> writeRow = processRowData(columns, row);
                    System.out.println(JSON.toJSONString(writeRow));
                    System.out.println("==============================");
                }

                break;
            }
            case DELETE_ROWS:
            case EXT_DELETE_ROWS: {
                DeleteRowsEventData deleteRowsEventData = event.getData();
                Table table = TableTool.getTableInfo(deleteRowsEventData.getTableId());
                Map<Integer, String> columns = table.getColumns();
                List<Serializable[]> rows = deleteRowsEventData.getRows();
                System.out.println("===========delete=============");
                for (Serializable[] row : rows) {
                    Map<String, Object> deleteRow = processRowData(columns, row);
                    System.out.println(JSON.toJSONString(deleteRow));
                    System.out.println("==============================");
                }
                break;
            }
            case UPDATE_ROWS:
            case EXT_UPDATE_ROWS: {
                UpdateRowsEventData updateRowsEventData = event.getData();
                List<Map.Entry<Serializable[], Serializable[]>> rows = updateRowsEventData.getRows();
                Table table = TableTool.getTableInfo(updateRowsEventData.getTableId());
                Map<Integer, String> columns = table.getColumns();

                System.out.println("===========update=============");
                for (Map.Entry<Serializable[], Serializable[]> row : rows) {
                    //更新后的数据
                    Serializable[] values = row.getValue();
                    //更新前的数据
                    Serializable[] keys = row.getKey();
                    Map<String, Object> before = processRowData(columns, keys);
                    Map<String, Object> after = processRowData(columns, values);
                    System.out.println("before:" + JSON.toJSONString(before));
                    System.out.println("after: " + JSON.toJSONString(after));
                    System.out.println("==============================");
                }
                break;
            }
            default: {
//                System.out.println(event.getHeader().toString());
//                System.out.println(JSON.toJSONString(event.getData()));
            }

        }
    }

    public static Map<String, Object> processRowData(Map<Integer, String> columns, Serializable[] row) {
        Map<String, Object> rowData = Maps.newHashMap();
        for (int i = 0; i < row.length; i++) {
            rowData.put(columns.get(i), row[i]);
        }
        return rowData;
    }
}
