package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.result.SimpleError;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

/**
 * Builds a new URI or clones an existing URI with optional updates.
 * Setter functions are idempotent.
 */
public class UriBuilder {
    public static final String PATH_DELIMITER = "/";
    public static final String QUERY_ASSIGN_OP = "=";
    private String scheme;
    private String authority;
    private StringBuilder path = new StringBuilder();
    private Map<String, QueryElement> queryElementsByKey = new HashMap<>();
    private String fragment = null;

    public UriBuilder() {

    }

    public UriBuilder(URI uriToUpdate) {
        scheme = uriToUpdate.getScheme();
        authority = uriToUpdate.getRawAuthority();
        path.append(uriToUpdate.getRawPath());
        setQuery(uriToUpdate.getQuery(), queryElementsByKey);
    }

    private static void setQuery(
        String query, Map<String, QueryElement> queryElementsByKey) {
        if (!StringUtils.isEmpty(query)) {
            String[] qElems = query.split("&");
            for (String qe : qElems) {
                String[] fields = qe.split(QUERY_ASSIGN_OP);
                final String key = fields[0];
                queryElementsByKey.put(key, new QueryElement(key, fields[1]));
            }
        }
    }

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

    /**
     * Will replace queryElement with same key if present.
     * @param key
     * @param value
     * @return
     */
    public UriBuilder queryElement(String key, String value) {
        queryElementsByKey.put(key, new QueryElement(key, value));
        return this;
    }

    public UriBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public Result<URI> build() {
        URI uri;
        String query = "";
        if (queryElementsByKey.size() > 0) {
            StringBuilder qb = new StringBuilder();
            for (Map.Entry qe : queryElementsByKey.entrySet()) {
                String value = ((QueryElement)qe.getValue()).value;
                qb.append(qe.getKey())
                    .append("=")
                    .append(value)
                    .append("&");
            }
            qb.deleteCharAt(qb.length() - 1);
            query = qb.toString();
        }
        try {
            uri = new URI(
                scheme, authority, path.toString(), query,
                fragment);
        } catch (Exception e) {
            return failure(SimpleError.fromException(e));
        }
        return success(uri);
    }

    public UriBuilder replaceQuery(String key, String value) {
        clearQuery();
        return queryElement(key, value);
    }

    public UriBuilder clearQuery() {
        queryElementsByKey.clear();
        return this;
    }

}

class QueryElement {
    public final String key;
    public final String value;

    public QueryElement(String fieldName, String value) {
        this.key = fieldName;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryElement element = (QueryElement) o;
        return Objects.equals(key, element.key) &&
            Objects.equals(value, element.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, value);
    }
}

