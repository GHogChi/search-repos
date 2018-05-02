package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.HttpStatus;

import java.util.Collections;
import java.util.Map;

//todo need to handle input streams
public class WebResponse {
    public final String body;
    public final HttpStatus status;
    private final Map<String, Map<String, String>> headersByName;

    public WebResponse(String body, HttpStatus status,
                       Map<String, Map<String, String>> headersByName) {
        this.body = body;
        this.status = status;
        this.headersByName = headersByName;
    }

    public Map<String, Map<String, String>> getHeaders(){
        return Collections.unmodifiableMap(headersByName);
    }
}
