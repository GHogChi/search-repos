package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.Result;

import java.net.URI;

/**
 * Currently supports only GET.
 * TODO add remaining methods
 */
public abstract class HttpPort {
    public abstract Result<WebResponse> get(URI uri);
}
