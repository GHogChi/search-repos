package com.spenkana.exp.searchrepos.domain;

import com.spenkana.exp.searchrepos.support.io.ApacheHttpPort;
import com.spenkana.exp.searchrepos.support.io.HttpPort;
import com.spenkana.exp.searchrepos.support.io.UriBuilder;
import com.spenkana.exp.searchrepos.support.io.WebResponse;
import com.spenkana.exp.searchrepos.support.io.json.JsonAdapter;
import com.spenkana.exp.searchrepos.support.io.json.JsonExtractor;
import com.spenkana.exp.searchrepos.support.io.json.JsonTokenBindings;
import com.spenkana.exp.searchrepos.support.io.json.jackson.JacksonBindings;
import com.spenkana.exp.searchrepos.support.io.json.jackson
    .JacksonJsonExtractor;
import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.stateMachine.ActiveHandlerGroup;
import com.spenkana.exp.searchrepos.support.stateMachine.EventHandler;
import com.spenkana.exp.searchrepos.support.stateMachine.FieldHandler;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.spenkana.exp.searchrepos.domain
    .GitHubRepositorySearchAdapter.REPO_SEARCH_BASE_URI;
import static com.spenkana.exp.searchrepos.domain
    .GitHubRepositorySearchAdapter.submitWebRequest;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static com.spenkana.exp.searchrepos.support.stateMachine.FieldHandler
    .createFieldHandler;
import static org.junit.jupiter.api.Assertions.fail;

//TODO implement the query DSL

/**
 * This is a domain class: the domain is testing GitHub search APIs.
 * Setting up event handlers for a state machine is too low level for a
 * domain class. There should be a query DSL for communicating with the
 * search adapter (implementing a read-only repository pattern, translating
 * the DSL to the GitHub API and building the query-specific parser on a
 * state machine).
 */
public class WhenGitHubReposAreSearched {
    //todo inject these concrete items via the test runner
    private final HttpPort webPort = new ApacheHttpPort();
    JsonTokenBindings bindings = (new JacksonBindings()).bindTokens();
    static final String TARGET = "etri";

    @Test
    public void allReposSelectedByNameHaveMatchingNames() {
        URI nameMatchBaseUri = new UriBuilder(REPO_SEARCH_BASE_URI)
            .queryElement("q", TARGET + " in:name")
            .queryElement("per_page", "30")
            .build()
            .output;
        Result<Integer> pageCountResult =
            GitHubRepositorySearchAdapter.totalPages(nameMatchBaseUri);
        if (pageCountResult.failed())
            fail(pageCountResult.getErrorMessage());
        List<FieldHandler> fieldHandlers = buildNameMatchHandler();
        IntStream.range(1, pageCountResult.output + 1)
            .forEachOrdered(n -> {
                URI pagedUri = new UriBuilder(nameMatchBaseUri)
                    .queryElement("page", "" + n)
                    .build()
                    .output;
                Result<WebResponse> responseResult = submitWebRequest(pagedUri,
                    new ApacheHttpPort());
                if (responseResult.failed()) {
                    fail(responseResult.getErrorMessage());
                }
                String body = responseResult.output.body;
                //todo move parser brand lockin upward, use abstraction here
                Result<JsonExtractor> extractorResult = JacksonJsonExtractor
                    .extractorFor(
                    body, bindings
                );
            });
    }

    @Test
    public void allReposSelectedByNameHaveMatchingNamesAndOrFullNames() {
        URI nameMatchBaseUri = new UriBuilder(REPO_SEARCH_BASE_URI)
            .queryElement("q", TARGET + " in:name")
            .queryElement("per_page", "30")
            .build()
            .output;

        JsonAdapter jsonAdapter = new JsonAdapter();
        EventHandler nameHandler = jsonAdapter
            .makeFieldExtractor("name extractor", "name").output;
        EventHandler fullNameHandler = jsonAdapter
            .makeFieldExtractor("full name extractor", "full_name")
            .output;
        ActiveHandlerGroup repositoryInfo = new ActiveHandlerGroup(
            "repo info", nameHandler, fullNameHandler
        );
    }

    private Result<JsonAdapter> getJsonEventProvider(URI
                                                               nameMatchBaseUri) {

        return success(new JsonAdapter());
    }


    private List<FieldHandler> buildNameMatchHandler() {
        List<FieldHandler> handlers = new ArrayList<>();
        handlers.add(createFieldHandler("name",
            s -> (((String) s).toLowerCase()
                .contains(TARGET.toLowerCase()))
                ? Result.success()
                : failure(MessageFormat.format(
                "Name \"{0}\" does not contain target string: {1}",
                s, TARGET
            )), FieldHandler.FieldType.STRING).output);
        return handlers;
    }

}
