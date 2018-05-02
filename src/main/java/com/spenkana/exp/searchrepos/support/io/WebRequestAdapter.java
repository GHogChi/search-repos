package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.Result;

import java.net.URI;

import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static org.junit.jupiter.api.Assertions.assertTrue;
//todo split vertically into an abstract class and specialized subclasses
public class WebRequestAdapter {
    public static Result<WebResponse> submitWebRequest(URI uri) {
        ApacheHttpPort port = new ApacheHttpPort();
        Result<WebResponse> webResult = port.get(uri);
        assertTrue(webResult.succeeded());
        return success(webResult.getOutput());
    }
}
