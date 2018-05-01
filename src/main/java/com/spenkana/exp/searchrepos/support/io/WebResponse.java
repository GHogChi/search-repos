package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.HttpStatus;

public class WebResponse {
    public final Json body;
    public final HttpStatus status;

    public WebResponse(Json body,
                       HttpStatus status) {
        this.body = body;
        this.status = status;
    }

    public Json getBody() {
        return body;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
