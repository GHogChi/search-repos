package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.FieldHandler;
import com.spenkana.exp.searchrepos.support.result.HttpStatus;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.spenkana.exp.searchrepos.support.FieldHandler.FieldType
    .BOOLEAN;
import static com.spenkana.exp.searchrepos.support.FieldHandler.FieldType
    .INTEGER;
import static com.spenkana.exp.searchrepos.support.FieldHandler
    .createFieldHandler;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static org.junit.jupiter.api.Assertions.*;

//TODO separate "slow" integration tests like this from fast unit tests
public class WhenThePortSendsARestfulGetRequest {
    int count = -1;
    boolean incompleteResults = true;

    @Test
    public void itReturnsTheJsonResponse() throws IOException {
        Result<WebResponse> result = getTheWebResponse();

        String content = result.getOutput().body.asString;
        List<FieldHandler> handlers = new ArrayList<>();
        handlers.add(createFieldHandler("total_count",
            n -> {
                try {
                    setCount((Integer) n);
                } catch (Exception e) {
                    return failure(e);
                }
                return success();
            }, INTEGER).output);

        handlers.add(createFieldHandler("incomplete_results",
            b -> {
                try {
                    setIncompleteResults((Boolean) b);
                } catch (Exception e) {
                    return failure(e);
                }
                return success();
            }, BOOLEAN).getOutput());
        Result<Void> parseResult = Json.parse(content, handlers);
        assertTrue(parseResult.succeeded());
        assertEquals(count, 0);
        assertEquals(incompleteResults, false);
    }

    private void setIncompleteResults(Boolean b) {
        incompleteResults = b;
    }

    private void setCount(Integer n) {
        count = n;
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
