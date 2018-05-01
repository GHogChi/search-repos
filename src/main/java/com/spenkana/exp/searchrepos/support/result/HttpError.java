package com.spenkana.exp.searchrepos.support.result;

public class HttpError extends SafeError {
    public final HttpStatus status;

    public HttpError(String msg, HttpStatus status) {
        super(msg);
        this.status = status;
    }
}
