package com.newbig.app.util;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

public class CustomDateEditor extends PropertyEditorSupport {
    private static final List<String> formarts = Lists.newArrayList();

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
        formarts.add("yyyy-MM-ddTHH:mm");
        formarts.add("yyyy-MM-ddTHH:mm:ss");
    }

    //    private final DateFormat dateFormat;
    private final boolean allowEmpty;
    private final int exactDateLength;

    public CustomDateEditor(boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        DateTimeFormatter dtf = DateTimeFormat.forPattern(format);
        DateTime dateTime = DateTime.parse(dateStr, dtf);
        return dateTime.toDate();
    }

//    public String getAsText() {
//        Date value = (Date) this.getValue();
//        DateFormat dateFormat = new SimpleDateFormat(format);
//        return value != null ? this.dateFormat.format(value) : "";
//    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !org.springframework.util.StringUtils.hasText(text)) {
            this.setValue((Object) null);
        } else {
            if (text != null && this.exactDateLength >= 0) {
                throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
            }
            this.setValue(this.convert(text));
        }

    }

    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(3));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}T{1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source.replace("T", " "), formarts.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source.replace("T", " "), formarts.get(3));
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }
}
