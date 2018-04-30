package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.ErrorList;
import com.spenkana.exp.searchrepos.support.result.HttpError;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class WhenHttpAdapterSendsGetRequest {
    public static final String ACCEPT_HEADER =
        "Accept: application/vnd.github.v3+json ";

    @Test
    public void itReturnsResponse() {
        ApacheHttpAdapter adapter = new ApacheHttpAdapter();
        URI uri = new UriBuilder().scheme("https").authority("api.github.com")
            .path("search", "repositories").query("q", "lunch")
            .query("per_page", "1").query("page", "1").build();
        Result<Json, HttpError> response = adapter.get(uri);
    }

    private class UriBuilder {
        public static final String PATH_DELIMITER = "/";
        private String scheme;
        private String authority;
        private StringBuilder path = new StringBuilder();
        private List<String> queryList = new ArrayList<>();

        public UriBuilder scheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        public UriBuilder authority(String authority) {
            this.authority = authority;
            return this;
        }

        public UriBuilder path(String... elements) {
            for (String element : elements) {
                path.append(PATH_DELIMITER).append(element);
            }
            return this;
        }

        public UriBuilder query(String key, String value) {
            queryList.add(key + "=" + value);
            return this;
        }

        public URI build() {
            return null;
        }
    }
}
