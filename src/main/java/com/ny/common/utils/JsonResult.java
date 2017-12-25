package com.ny.common.utils;

public class JsonResult {
    private long total;
    private String message;
    private boolean status;
    private Object data;

    public JsonResult() {
        this.status = true;
    }

    public JsonResult(String msg, boolean status) {
        this.message = msg;
        this.status = status;
    }

    public JsonResult(long total, Object data) {
        this.data = data;
        this.total = total;
        this.status = true;
    }

    public JsonResult(long total, String msg, Object data) {
        this.data = data;
        this.total = total;
        this.message = msg;
        this.status = true;
    }

    public JsonResult(long total, String msg, boolean status, Object data) {
        this.total = total;
        this.message = msg;
        this.status = status;
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
