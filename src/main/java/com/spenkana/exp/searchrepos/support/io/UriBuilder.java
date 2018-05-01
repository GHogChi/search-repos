package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.result.SimpleError;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

//TODO use Apache HttpCore UriBuilder instead
class UriBuilder {
    public static final String PATH_DELIMITER = "/";
    private String scheme;
    private String authority;
    private StringBuilder path = new StringBuilder();
    private List<String> queryList = new ArrayList<>();
    private String fragment = null;

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
            path.append(PATH_DELIMITER)
                .append(element);
        }
        return this;
    }

    public UriBuilder query(String key, String value) {
        queryList.add(key + "=" + value);
        return this;
    }

    public Result<URI> build() {
        URI uri;
        try {
            String query = String.join("&", queryList);
            uri = new URI(scheme, authority, path.toString(), query, fragment);
        } catch (Exception e) {
            return failure(SimpleError.fromException(e));
        }
        return success(uri);
    }
}
