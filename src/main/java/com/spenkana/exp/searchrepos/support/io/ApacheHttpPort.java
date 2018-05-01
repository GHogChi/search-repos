package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;

import static com.spenkana.exp.searchrepos.support.io.Json.fromString;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

public class ApacheHttpPort {

    String dummy = "{\"total_count\": 0,\"incomplete_results\": false," +
        "\"items\": [ ]}";

    public Result<WebResponse> get(URI uri) {
        Result<WebResponse> result = submitRequest(uri);
        if (result.succeeded()) {
            return success(result.getOutput());
        }
        return failure(result.getErrorMessage());
    }

    private Result<WebResponse> submitRequest(URI uri) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
        } catch (IOException e1) {
            cleanUp(response);
            return failure(new ExceptionalError(e1));
        }
        HttpEntity entity = response.getEntity();
        Result<HttpStatus>
            statusResult = HttpStatus.forCode(
            response.getStatusLine()
                .getStatusCode());
        if (statusResult.failed()) {
            cleanUp(response);
            return failure(statusResult.error);
        }
        String jsonString = null;
        try {
            jsonString = EntityUtils.toString(entity);
        } catch (IOException e1) {
            cleanUp(response);
            return failure(new ExceptionalError(e1));
        }
        final Result<Json> jsonResult = fromString(jsonString);
        if (jsonResult.failed()) {
            cleanUp(response);
            return failure(jsonResult.getErrorMessage());
        }
        cleanUp(response);
        return success(new WebResponse(
            jsonResult.getOutput(),
            statusResult.getOutput())
        );

    }

    private void cleanUp(CloseableHttpResponse response) {
        if (response != null) {

            try {
                response.close();
            } catch (IOException e) {
                //TODO decide if worth reporting; if so, how?
            }
        }
    }

}

