package com.lionel.gonews.data.remote;

public class ErrorInfo {
    public boolean isError;
    public String msg;

    public ErrorInfo(boolean isError, String msg) {
        this.isError = isError;
        this.msg = msg;
    }
}
