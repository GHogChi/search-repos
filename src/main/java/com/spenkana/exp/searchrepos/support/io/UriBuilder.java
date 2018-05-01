package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.result.SimpleError;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

class UriBuilder {
    public static final String PATH_DELIMITER = "/";
    public static final String QUERY_ASSIGN_OP = "=";
    private String scheme;
    private String authority;
    private StringBuilder path = new StringBuilder();
    private List<QueryElement> queryList = new ArrayList<>();
    private String fragment = null;

    public UriBuilder() {

    }

    public UriBuilder(URI uriToUpdate) {
        scheme = uriToUpdate.getScheme();
        authority = uriToUpdate.getRawAuthority();
        path.append(uriToUpdate.getRawPath());
        setQuery(uriToUpdate.getQuery(), queryList);
    }

    private static void setQuery(String query, List<QueryElement> queryList) {
        if (!StringUtils.isEmpty(query)) {
            String[] qElems = query.split("&");
            for (String qe : qElems) {
                String[] fields = qe.split(QUERY_ASSIGN_OP);
                queryList.add(new QueryElement(fields[0], fields[1]));
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

    public UriBuilder query(String key, String value) {
        queryList.add(new QueryElement(key, value));
        return this;
    }

    public Result<URI> build() {
        URI uri;
        String query = "";
        if (queryList.size() > 0) {
            StringBuilder qb = new StringBuilder();
            for (QueryElement qe : queryList) {
                qb.append(qe.key)
                    .append("=")
                    .append(qe.value)
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

    public UriBuilder replaceQuery(String q, String value) {
        queryList.clear();
        return query(q, value);
    }
}

class QueryElement {
    public final String key;
    public final String value;

    public QueryElement(String fieldName, String value) {
        this.key = fieldName;
        this.value = value;
    }
}

