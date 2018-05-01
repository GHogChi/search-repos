package com.spenkana.exp.searchrepos.support.io;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spenkana.exp.searchrepos.support.FieldHandler;
import com.spenkana.exp.searchrepos.support.result.ErrorList;
import com.spenkana.exp.searchrepos.support.result.ExceptionalError;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

//TODO remove redundancy in methods
public class Json {
    final String asString;
    final Class<?> theClass;
    private static JsonFactory factory;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Json(Object obj) throws JsonProcessingException {
        this(obj, obj.getClass());
    }

    private Json(Object obj, Class<?> theClass) throws JsonProcessingException {
        asString = objectMapper.writeValueAsString(obj);
        this.theClass = theClass;
    }

    public static Result<Void> parse(
        String jsonString, List<FieldHandler> handlers) {
        if (handlers == null || handlers.size() == 0) {
            return failure("No handlers passed");
        }
        Map<String, FieldHandler> handlersByFieldName = new HashMap<>();
        for (FieldHandler handler : handlers) {
            handlersByFieldName.put(handler.fieldName, handler);
        }
        Result<JsonParser> result = getStreamParser(jsonString);
        if (result.failed()){
            return failure(result.error);
        }
        JsonParser parser = result.output;
        ErrorList errors = new ErrorList();
        try {
            for (JsonToken token = null;
                 token != END_OBJECT;
                 token = parser.nextToken()) {
                if (token != FIELD_NAME) continue;
                String fieldName = parser.getCurrentName();
                if (handlersByFieldName.containsKey(fieldName)) {
                    FieldHandler handler = handlersByFieldName.get(fieldName);
                    parser.nextToken();
                    Object currentValue = getFieldValue(parser, handler);
                    Result<Void> handlerResult =
                        handler.handle(currentValue);
                    if (handlerResult.failed()) {
                        errors.add(handlerResult.error);
                    }
                }
            }
        } catch (Exception e) {
            errors.add(e);
        }
        return errors.errorCount() > 0
            ? failure(errors.asError())
            : success();
    }

    private static Object getFieldValue(JsonParser parser, FieldHandler handler)
        throws IOException {
        Object currentValue = null;
        switch (handler.fieldType){
            case INTEGER:
                currentValue = parser.getIntValue();
                break;
            case BOOLEAN:
                currentValue = parser.getBooleanValue();
                break;
            case STRING:
                currentValue = parser.getText();
                break;
        }
        return currentValue;
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

    @Override
    public String toString() {
        return asString;
    }

    private static Result<JsonParser> getStreamParser(
        String jsonString) {
        JsonParser parser = null;
        if (factory == null) {
            factory = new JsonFactory();
            try {
                parser = factory.createParser(jsonString);
            } catch (Exception e) {
                return failure(e);
            }
        }
        return success(parser);
    }

    public Result<Object> asObject() {
        try {
            Object object = new ObjectMapper().readValue(asString, theClass);
            return success(object);
        } catch (Exception e) {
            return failure(e);
        }
    }
}
