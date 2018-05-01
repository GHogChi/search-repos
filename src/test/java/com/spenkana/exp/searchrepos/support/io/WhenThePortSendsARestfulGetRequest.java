package com.spenkana.exp.searchrepos.support.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.spenkana.exp.searchrepos.support.result.HttpStatus;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;

import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

//TODO separate "slow" integration tests like this from fast unit tests
public class WhenThePortSendsARestfulGetRequest {
    private JsonFactory jfactory = new JsonFactory();
    private JsonParser jParser;

    @Test
    public void itReturnsTheJsonResponse() throws IOException {
        Result<WebResponse> result = getTheWebResponse();

        final String content = result.getOutput().body.asString;
//            if (fieldResult.failed()){
//                fail(fieldResult.getErrorMessage());
//            }
//        }
    }


    @Test
    public void itReturnsTheStatus() {
        Result<WebResponse> result = getTheWebResponse();

        assertSame(result.getOutput().status, HttpStatus.SUCCESS);

    }


    private Result<WebResponse> getTheWebResponse() {
        ApacheHttpPort port = new ApacheHttpPort();
        Result<URI> result = new UriBuilder()
            .scheme("https")
            .authority("api.github.com")
            .path("search", "repositories")
            .query("q", "notARepoNameWeHope")
            .build();
        URI uri = result.getOutput();
        Result<WebResponse> webResult = port.get(uri);
        assertTrue(result.succeeded());
        return success(webResult.getOutput());
    }

}
