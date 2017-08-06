package com.newbig.app.imagesearch.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@AllArgsConstructor
public class ResponseVO<T> {
    private Integer code;
    private Boolean status;
    private String message;
    private T result;

    public static ResponseVO success(Object o){
        return new ResponseVO(200,Boolean.TRUE,null,o);
    }
    public static ResponseVO failure(String message){
        return new ResponseVO(200,Boolean.FALSE, message,null);
    }
}
