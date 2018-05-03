package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.fail;

public class WhenWebRequestAdapterSendsARequest {
    public static final String ACCEPT_HEADER =
        "Accept: application/vnd.github.v3+json ";

    @Test
    public void itReturnsResponse() {
        ApacheHttpPort adapter = new ApacheHttpPort();
        Result<URI> uriresult =
            new UriBuilder().scheme("https")
                .authority("api.github.com")
                .path("search", "repositories")
                .queryElement("q", "lunch")
                .queryElement("per_page", "1")
                .queryElement("page", "1")
                .build();
        if (uriresult.failed()){
            fail(uriresult.getErrorMessage());
        }
        URI uri = uriresult.getOutput();
        Result<WebResponse> responseResult = adapter.get(uri);
        if(responseResult.failed()){
            fail(responseResult.getErrorMessage());
        }
    }

}
