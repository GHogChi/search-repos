package com.spenkana.exp.searchrepos.domain;

import com.spenkana.exp.searchrepos.support.io.ApacheHttpPort;
import com.spenkana.exp.searchrepos.support.io.HttpPort;
import com.spenkana.exp.searchrepos.support.io.UriBuilder;
import com.spenkana.exp.searchrepos.support.io.WebResponse;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static org.junit.jupiter.api.Assertions.assertTrue;

//todo split vertically into an abstract class and specialized subclasses
public class GitHubRepositorySearchAdapter {

    public static final URI REPO_SEARCH_BASE_URI = new UriBuilder()
        .scheme("https")
        .authority("api.github.com")
        .path("search", "repositories")
        .build()
        .output;
    public static final String LINK_HEADER_KEY = "Link";

    public static Result<WebResponse> submitWebRequest(URI uri, HttpPort port) {
        Result<WebResponse> webResult = port.get(uri);
        return (webResult.succeeded())
            ? success(webResult.output)
            : failure(webResult.getErrorMessage());
    }

    private static final Pattern PAGE_NO = Pattern.compile(
        "[^_]page=(\\d)[^>]*>;\\s*rel=\"last"
//        "page="
    );

    //        "[^_]page=(\\d)[^>]*>;\\s*rel=\\\"last");
    //todo get headers only using HEAD method (this implementation is
    // inefficient)
    public static Result<Integer> totalPages(URI uri) {
        Result<WebResponse> responseResult = submitWebRequest(uri,
            new ApacheHttpPort());
        if (responseResult.failed()) {
            return failure(responseResult.getErrorMessage());
        }
        Map<String, List<String>> headers =
            responseResult.output.getHeaders();
        if (headers.containsKey(LINK_HEADER_KEY)) {
            List<String> elements = headers.get(LINK_HEADER_KEY);
            String wholeLink = elements.get(0);
            Matcher m = PAGE_NO.matcher(wholeLink);
            m.find();

            if (m.groupCount() == 1) {
                int pageCount = Integer.parseInt(m.group(1));
                return success(pageCount);
            }
            return failure("Last page not found in Link header");
        }
        return failure("Link header not found");
    }
}
