package com.spenkana.exp.searchrepos.support;

import com.spenkana.exp.searchrepos.support.result.Result;
import com.spenkana.exp.searchrepos.support.result.SimpleError;
import org.codehaus.plexus.util.StringUtils;

import java.text.MessageFormat;

import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

public class FieldExpectation {
    public final String fieldname;
    public final Object expectedValue;
    public final Class<?> expectedClass;

    public FieldExpectation(
        String fieldname, Object expectedValue,
        Class<?> expectedClass) {
        this.fieldname = fieldname;
        this.expectedValue = expectedValue;
        this.expectedClass = expectedClass;
    }

    public static Result<FieldExpectation> createExpectation(
        String fieldname, Object expectedValue,
        Class<?> expectedClass) {
        if (!expectedClass.isAssignableFrom(expectedValue.getClass())
            ) {
            return failure(MessageFormat.format(
                "Expected {0}, got {1}",
                expectedClass.getName(), expectedValue.getClass().getName()
            ));
        }
        if (StringUtils.isEmpty(fieldname)){
            return failure("Field name cannot be empty");
        }
        return success(
            new FieldExpectation(fieldname, expectedValue, expectedClass));
        }
}
