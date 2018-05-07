package com.spenkana.exp.searchrepos.support.io.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spenkana.exp.searchrepos.support.result.ExceptionalError;
import com.spenkana.exp.searchrepos.support.result.Result;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

//TODO parsing needs bulletproofing - it ignores structure -
//e.g., will match fields at any level
public class Json {
    private final String asString;
    public final Class<?> theClass;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Json(Object obj) throws JsonProcessingException {
        this(obj, obj.getClass());
    }

    private Json(Object obj, Class<?> theClass) throws JsonProcessingException {
        asString = objectMapper.writeValueAsString(obj);
        this.theClass = theClass;
    }

    @Override
    public String toString() {
        return asString;
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
  public static Result<Json> fromObject(Object obj) {
        Json json;
        try {
            json = new Json(obj);
        } catch (JsonProcessingException e) {
            return failure(new ExceptionalError(e));
        }
        return success(json);
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
