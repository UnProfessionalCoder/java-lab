package com.newbig.app.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.newbig.app.util.CustomDateEditor.parseDate;

public class CustomerDateTimeDeserialize extends JsonDeserializer<Date> {
    private static final List<String> formarts = Lists.newArrayList();

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
        formarts.add("HH:mm");
        formarts.add("HH:mm:ss");
    }


//    public String getAsText() {
//        Date value = (Date) this.getValue();
//        DateFormat dateFormat = new SimpleDateFormat(format);
//        return value != null ? this.dateFormat.format(value) : "";
//    }


    @Override
    public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext) throws IOException {
        String value = paramJsonParser.getText().trim();
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        if (value.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(value, formarts.get(0));
        } else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(value, formarts.get(1));
        } else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(value, formarts.get(2));
        } else if (value.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(value, formarts.get(3));
        } else if (value.matches("^\\d{1,2}:\\d{1,2}$")) {
            return parseDate(value, formarts.get(4));
        } else if (value.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(value, formarts.get(5));
        } else {
            return null;
        }
    }
}

