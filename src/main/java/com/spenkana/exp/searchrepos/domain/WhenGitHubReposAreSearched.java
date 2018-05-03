package com.spenkana.exp.searchrepos.domain;

import com.spenkana.exp.searchrepos.support.FieldHandler;
import com.spenkana.exp.searchrepos.support.io.GitHubRepositorySearchAdapter;
import com.spenkana.exp.searchrepos.support.io.UriBuilder;
import com.spenkana.exp.searchrepos.support.io.WebResponse;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.spenkana.exp.searchrepos.support.DebugUtils.dump;
import static com.spenkana.exp.searchrepos.support.FieldHandler.FieldType
    .STRING;
import static com.spenkana.exp.searchrepos.support.FieldHandler
    .createFieldHandler;
import static com.spenkana.exp.searchrepos.support.io
    .GitHubRepositorySearchAdapter.REPO_SEARCH_BASE_URI;
import static com.spenkana.exp.searchrepos.support.io
    .GitHubRepositorySearchAdapter.submitWebRequest;
import static com.spenkana.exp.searchrepos.support.io.Json.parse;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static org.junit.jupiter.api.Assertions.fail;

public class WhenGitHubReposAreSearched {
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
                Result<WebResponse> responseResult = submitWebRequest(pagedUri);
                if (responseResult.failed()) {
                    fail(responseResult.getErrorMessage());
                }
                String body = responseResult.output.body;
                Result<Void> parseResult = parse(body, fieldHandlers);
                if (!parseResult.succeeded()) {
                    fail(parseResult.getErrorMessage());
                }
            });

    }


    @Test
    public void matchSpike() {
        String y = "<https://api.github" +
            ".com/search/repositories?q=etri+in%3Aname&per_page=30&page=2>; " +
            "rel=\"next\", <https://api.github" +
            ".com/search/repositories?q=etri+in%3Aname&per_page=30&page=5>; " +
            "rel=\"last\"";
        Pattern p = Pattern.compile("[^_]page=(\\d)[^>]*>;\\s*rel=\"last");
        Matcher m = p.matcher(y);
        while (m.find()) {
            dump("group count: " + m.groupCount());
            for (int i = 0; i <= m.groupCount(); ++i) {
                dump("group[" + i + "]: " + m.group(i));
            }
        }
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
            )), STRING).output);
        return handlers;
    }

}
