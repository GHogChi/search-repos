package com.spenkana.exp.searchrepos.domain;

import com.spenkana.exp.searchrepos.support.FieldHandler;
import com.spenkana.exp.searchrepos.support.io.UriBuilder;
import com.spenkana.exp.searchrepos.support.io.WebResponse;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.spenkana.exp.searchrepos.support.FieldHandler.FieldType
    .STRING;
import static com.spenkana.exp.searchrepos.support.FieldHandler
    .createFieldHandler;
import static com.spenkana.exp.searchrepos.support.io.Json.parse;
import static com.spenkana.exp.searchrepos.support.io.WebRequestAdapter
    .submitWebRequest;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static org.junit.jupiter.api.Assertions.fail;

public class WhenGitHubReposAreSearched {
    static final String TARGET = "etri";

    @Test
    public void allReposSelectedByNameHaveMatchingNames() {
        URI uri = new UriBuilder()
            .scheme("https")
            .authority("api.github.com")
            .path("search", "repositories")
            .query("q", TARGET + " in:name")
            .build().output;
        Result<WebResponse> responseResult = submitWebRequest(uri);
        if(responseResult.failed()){
            fail(responseResult.getErrorMessage());
        }
        String body = responseResult.output.body;
        List<FieldHandler> fieldHandlers = buildHandlers();
        Result<Void> parseResult = parse(body, fieldHandlers);
        if(!parseResult.succeeded()){
            fail(parseResult.getErrorMessage());
        }

    }

    private List<FieldHandler> buildHandlers() {
        List<FieldHandler> handlers = new ArrayList<>();
        handlers.add(createFieldHandler("name",
            s-> (((String)s).toLowerCase().contains(TARGET.toLowerCase()))
                ? Result.success()
                : failure(MessageFormat.format(
                    "Name \"{0}\" does not contain target string: {1}",
                s,TARGET
            )), STRING).output);
        return handlers;
    }

}
