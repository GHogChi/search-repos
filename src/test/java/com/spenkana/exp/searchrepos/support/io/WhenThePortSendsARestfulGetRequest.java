package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.domain.GitHubRepositorySearchAdapter;
import com.spenkana.exp.searchrepos.support.io.json.JsonAdapter;
import com.spenkana.exp.searchrepos.support.io.json.jackson.Json;
import com.spenkana.exp.searchrepos.support.result.HttpStatus;
import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.stateMachine.FieldHandler;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static com.spenkana.exp.searchrepos.support.stateMachine.FieldHandler
    .FieldType.BOOLEAN;
import static com.spenkana.exp.searchrepos.support.stateMachine.FieldHandler
    .FieldType.INTEGER;
import static com.spenkana.exp.searchrepos.support.stateMachine.FieldHandler
    .createFieldHandler;
import static org.junit.jupiter.api.Assertions.*;

//TODO separate "slow" integration tests like this from fast unit tests
public class WhenThePortSendsARestfulGetRequest {
    private int count = -1;
    private boolean incompleteResults = true;

    @Test
    public void itReturnsTheJsonResponse() {
        Result<WebResponse> result = getTheWebResponse();

        String content = result.getOutput().body;
        List<FieldHandler> handlers = buildFieldHandlers();
        JsonAdapter adapter = new JsonAdapter();
        Result<Json> parseResult = Json.fromString(content);
        assertTrue(parseResult.succeeded());
        assertEquals(0, count);
        assertFalse(incompleteResults);
    }

    private List<FieldHandler> buildFieldHandlers() {
        List<FieldHandler> handlers = new ArrayList<>();
        handlers.add(createFieldHandler("total_count",
            n -> {
                setCount((Integer) n);
                return success();
            }, INTEGER).output);

        handlers.add(createFieldHandler("incomplete_results",
            b -> {
                setIncompleteResults((Boolean) b);
                return success();
            }, BOOLEAN).getOutput());
        return handlers;
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

        assertSame(result.getOutput().status, HttpStatus.OK);

    }


    private Result<WebResponse> getTheWebResponse() {
        Result<URI> result = new UriBuilder()
            .scheme("https")
            .authority("api.github.com")
            .path("search", "repositories")
            .queryElement("q", "notARepoNameWeHope")
            .build();
        URI uri = result.getOutput();
        return GitHubRepositorySearchAdapter.submitWebRequest(uri,
            new ApacheHttpPort());
    }

}
