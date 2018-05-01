package com.spenkana.exp.searchrepos.support.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spenkana.exp.searchrepos.support.FieldExpectation;
import com.spenkana.exp.searchrepos.support.result.ErrorList;
import com.spenkana.exp.searchrepos.support.result.ExceptionalError;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static com.spenkana.exp.searchrepos.support.result.SimpleError
    .fromException;

//TODO remove redundancy in methods
public class Json {
    final String asString;
    final Class<?> theClass;
    private JsonFactory factory;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Json(Object obj) throws JsonProcessingException {
        this(obj, obj.getClass());
    }

    private Json(Object obj, Class<?> theClass) throws JsonProcessingException {
        asString = objectMapper.writeValueAsString(obj);
        this.theClass = theClass;
    }
    public static Result<Json> fromObject(Object obj) {
        Json json;
        try {
            json = new Json(obj);
        } catch (JsonProcessingException e) {
            return failure(new ExceptionalError(e));
        }
        return success(json);
    }

    public static Result<Json> fromString(String jsonString) {
        Json json;
        try {
            Object obj = new ObjectMapper().readValue(jsonString, Object.class);
            json = new Json(obj);
            return success(json);
        } catch (Exception e) {
            return failure(new ExceptionalError(e));
        }
    }

    public Result<Void> checkFieldValues(
        Map<String, FieldExpectation> expectationsByName) {
        Map<String, FieldExpectation> missingFields =
            new HashMap<>(expectationsByName);
        ErrorList errorList = new ErrorList();
        JsonParser parser = getParser(errorList);
        if (parser == null) {
            return failure(errorList.asError());
        }
        try {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                if (fieldName == null) continue;
                if (expectationsByName.containsKey(fieldName)) {
                    FieldExpectation expectation =
                        expectationsByName.get(fieldName);
                    switch (expectation.expectedClass.getName()) {
                        case "Integer":
                    }
                }
            }
        } catch (IOException e) {
            errorList.add(fromException(e));
        }
        return success();
    }

    private static Result<Void> checkBooleanFieldValue(
        String fieldname, boolean expectedValue, JsonParser jParser)
        throws IOException {
        jParser.nextToken();
        Boolean value = jParser.getBooleanValue();
        if (!value.equals(expectedValue)) {
            final String message = MessageFormat
                .format("Expected {0} to be {1}, got {2}",
                    fieldname, expectedValue, value);
            return failure(message);
        }
        return success();
    }

    private static Result<Void> checkStringFieldValue(
        String fieldname, String expectedValue, JsonParser jParser)
        throws IOException {
        jParser.nextToken();
        String value = jParser.getText();
        if (!value.equals(expectedValue)) {
            final String message = MessageFormat
                .format("Expected {0} to be {1}, got {2}",
                    fieldname, expectedValue, value);
            return failure(message);
        }
        return success();
    }

    private static Result<Void> checkIntFieldValue(
        String fieldname, int expectedValue, JsonParser jParser)
        throws IOException {
        jParser.nextToken();
        Integer value = jParser.getIntValue();
        if (!value.equals(expectedValue)) {
            final String message = MessageFormat
                .format("Expected {0} to be {1}, got {2}",
                    fieldname, expectedValue, value);
            return failure(message);
        }
        return success();
    }

    @Override
    public String toString() {
        return asString;
    }

    private JsonParser getParser(ErrorList errorList) {
        JsonParser parser = null;
        if (factory == null) {
            factory = new JsonFactory();
            try {
                parser = factory.createParser(asString);
            } catch (IOException e) {
                errorList.add(new ExceptionalError(e));
            }
        }
        return parser;
    }
}
