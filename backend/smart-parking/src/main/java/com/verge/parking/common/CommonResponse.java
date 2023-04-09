package com.verge.parking.common;

public class CommonResponse {
    private int code;
    private String message;
    private Object data;

    public CommonResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public CommonResponse() {
    }

    public static CommonResponse success() {
        return new CommonResponse(200, "success");
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse(200, "success", data);
    }

    public static CommonResponse fail() {
        return new CommonResponse(500, "fail");
    }

    public static CommonResponse fail(Object data) {
        return new CommonResponse(500, "fail", data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
