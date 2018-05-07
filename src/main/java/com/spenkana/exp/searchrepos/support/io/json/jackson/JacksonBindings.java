package com.spenkana.exp.searchrepos.support.io.json.jackson;

import com.fasterxml.jackson.core.JsonToken;
import com.spenkana.exp.searchrepos.support.io.json.JsonAdapter;
import com.spenkana.exp.searchrepos.support.io.json.JsonTokenBindings;
import com.spenkana.exp.searchrepos.support.io.json.JsonTokenBindings.Builder;

import static com.spenkana.exp.searchrepos.support.io.json.JsonAtomType.*;

public class JacksonBindings extends JsonAdapter.ProviderBindings {
    @Override
    public JsonTokenBindings bindTokens() {
        JsonTokenBindings tokenBindings = new Builder()
            .add(JsonToken.START_ARRAY, START_ARRAY)
            .add(JsonToken.END_ARRAY, END_ARRAY)
            .add(JsonToken.START_OBJECT,START_OBJECT)
            .add(JsonToken.END_OBJECT, END_OBJECT)
            .add(JsonToken.FIELD_NAME, FIELD_NAME)
            .add(JsonToken.VALUE_STRING, STRING)
            .add(JsonToken.VALUE_NUMBER_INT, LONG)
            .add(JsonToken.VALUE_NUMBER_FLOAT, DOUBLE)
            .add(JsonToken.VALUE_TRUE,TRUE)
            .add(JsonToken.VALUE_FALSE, FALSE)
            .add(JsonToken.VALUE_NULL, NULL)
            .build();
        return tokenBindings;
    }


}

