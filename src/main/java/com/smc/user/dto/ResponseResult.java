package com.smc.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ResponseResult<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public ResponseResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseResult<Object> fail(String message) {
        return new ResponseResult<>(-1, message, null);
    }

    public static <T> ResponseResult<T> success(String message, T data) {
        return new ResponseResult<>(0, message, data);
    }

}
