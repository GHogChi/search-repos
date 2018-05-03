package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;

//todo need to handle input streams
public class WebResponse {
    public final String body;
    public final HttpStatus status;
    private final Map<String,List<String>> parsedHeaders;

    public WebResponse(String body, HttpStatus status,
                       Map<String, List<String>> parsedHeaders) {
        this.body = body;
        this.status = status;
        this.parsedHeaders = parsedHeaders;
    }

    public Map<String, List<String>> getHeaders(){
        return Collections.unmodifiableMap(parsedHeaders);
    }
}
