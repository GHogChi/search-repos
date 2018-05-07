package com.spenkana.exp.searchrepos.support.stateMachine;

import com.spenkana.exp.searchrepos.support.result.Result;
import org.codehaus.plexus.util.StringUtils;

import java.util.function.Function;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

public class FieldHandler {
    public final String fieldName;
    public final Function<Object, Result<Void>> handler;
    public enum FieldType {INTEGER, BOOLEAN, STRING }
    public final FieldType fieldType;

    private FieldHandler(
        String fieldName, Function<Object, Result<Void>> handler,
        FieldType fieldType) {
        this.fieldName = fieldName;
        this.handler = handler;
        this.fieldType = fieldType;
    }

    public static Result<FieldHandler> createFieldHandler(
        String fieldName, Function<Object, Result<Void>> handler,
        FieldType fieldType) {
        if (StringUtils.isEmpty(fieldName)){
            return failure("Field name cannot be empty");
        }
        return success(
            new FieldHandler(fieldName, handler, fieldType));
        }

    public Result<Void> handle(Object value) {
        return handler.apply(value);
    }
}
