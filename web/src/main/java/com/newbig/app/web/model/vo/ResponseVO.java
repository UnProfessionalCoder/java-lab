package com.newbig.app.web.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by haibo on 2017/3/12.
 */
@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {
    private int code;
    private Boolean status;
    private String msg;
    private T result;

    public ResponseVO() {

    }

    public ResponseVO(int code, Boolean status, String msg, T result) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.result = result;
    }

    public static ResponseVO success(Object o) {
        return new ResponseVO(200, Boolean.TRUE, null, o);
    }

    public static ResponseVO failure(String message) {
        return new ResponseVO(200, Boolean.FALSE, message, null);
    }
}
