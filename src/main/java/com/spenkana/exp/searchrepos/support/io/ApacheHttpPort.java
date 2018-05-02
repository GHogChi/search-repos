package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.result.ExceptionalError;
import com.spenkana.exp.searchrepos.support.result.HttpStatus;
import com.spenkana.exp.searchrepos.support.result.Result;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.spenkana.exp.searchrepos.support.io.Json.fromString;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
//todo support streams and other RESTful commands
public class ApacheHttpPort {

    public static final String WHOLE_HEADER_VALUE = "WholeHeaderValue";

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
        Header[] headers = response.getAllHeaders();
        Map<String, Map<String, String>> headersByName = new HashMap<>();
        for(Header header: headers){
            HeaderElement[] elements = header.getElements();
            Map<String, String> elementsByName = new HashMap<>();
            elementsByName.put(WHOLE_HEADER_VALUE, header.getValue());
            for (HeaderElement element: elements){
                elementsByName.put(element.getName(), element.getValue());
            }
            headersByName.put(header.getName(), elementsByName);
        }
        String jsonString;
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
            jsonString,
            statusResult.getOutput(), headersByName)
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

