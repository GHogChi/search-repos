package com.spenkana.exp.searchrepos.support.io;

import com.spenkana.exp.searchrepos.support.io.json.jackson.Json;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.spenkana.exp.searchrepos.support.io.json.jackson.Json
    .fromString;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
//todo support streams and other RESTful commands
public class ApacheHttpPort extends HttpPort {

    public static final String WHOLE_HEADER_VALUE = "WholeHeaderValue";

    @Override
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
        Map<String, List<String>> parsedHeadersByName = getHeaders(response);
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
            jsonString, statusResult.getOutput(), parsedHeadersByName));

    }

    private Map<String, List<String>> getHeaders(
        CloseableHttpResponse response) {
        Header[] headers = response.getAllHeaders();
        Map<String, List<String>> parsedHeadersByName = new HashMap<>();
        for(Header header: headers){
            List<String> elements = new ArrayList<>();
            elements.add(header.getValue());
            for(HeaderElement element: header.getElements()){
                elements.add(element.getValue());
            }
            parsedHeadersByName.put(header.getName(), elements);
        }
        return parsedHeadersByName;
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

