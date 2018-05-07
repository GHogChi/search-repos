package com.spenkana.exp.searchrepos.support.io.json;

import com.spenkana.exp.searchrepos.support.result.Result;

import static com.spenkana.exp.searchrepos.support.io.json.JsonAtomType.BAD;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;
import static java.text.MessageFormat.format;

public class JsonAtom<T> {
    public static final JsonAtom EMPTY_ATOM = new JsonAtom(
        JsonAtomType.EMPTY, Object.class, new Object());
    public final JsonAtomType jsonAtomType;
    public final Class<?> theClass;
    private final Object value;

    public JsonAtom(
        JsonAtomType jsonAtomType, Class<?> theClass, Object value) {
        this.jsonAtomType = jsonAtomType;
        this.theClass = theClass;
        this.value = value;
    }

    public T getValue() {
        return (T) value;
    }

    public static Result<JsonAtom> makeAtom(
        JsonAtomType jsonType, Class<?> theClass, Object value) {
        return (theClass.isAssignableFrom(value.getClass()))
            ? success(new JsonAtom(jsonType, theClass, value))
            : failure(format(
            "Expected {0}, got {1}", theClass.getName(),
            value.getClass()
                .getName()));
    }


}
