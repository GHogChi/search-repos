package com.spenkana.exp.searchrepos.support.io.json.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.spenkana.exp.searchrepos.support.io.json.JsonAtom;
import com.spenkana.exp.searchrepos.support.io.json.JsonAtomType;
import com.spenkana.exp.searchrepos.support.io.json.JsonTokenBindings;
import com.spenkana.exp.searchrepos.support.io.json.JsonExtractor;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.io.IOException;

import static com.spenkana.exp.searchrepos.support.io.json.JsonAtom.EMPTY_ATOM;
import static com.spenkana.exp.searchrepos.support.io.json.JsonAtom.makeAtom;
import static com.spenkana.exp.searchrepos.support.io.json.JsonAtomType.*;
import static com.spenkana.exp.searchrepos.support.result.Result.failure;
import static com.spenkana.exp.searchrepos.support.result.Result.success;

public class JacksonJsonExtractor extends JsonExtractor {
    private final JsonParser parser;
    private final JsonTokenBindings tokenBindings;
    private boolean parseCompleted;
    private JsonAtomType currentToken = NO_TOKEN_PARSED_YET;

    public JacksonJsonExtractor(JsonParser jsonParser,
                                JsonTokenBindings tokenBindings) {
        parser = jsonParser;
        this.tokenBindings = tokenBindings;
    }

    @Override
    public Result<JsonAtom> safeGet() {
        JsonAtom atom = EMPTY_ATOM;
        switch (currentToken) {
            case NO_TOKEN_PARSED_YET:
            case EMPTY:
            case START_ARRAY:
            case END_ARRAY:
            case START_OBJECT:
            case END_OBJECT:
            case NOT_A_JSON_TOKEN:
            case END_OF_DOCUMENT:
                break;
            default:
                return constructAtom(currentToken);
        }
        return success(atom);
    }

    @Override
    public Result<JsonAtomType> nextAtomType() {
        if (parseCompleted) {
            return success(JsonAtomType.END_OF_DOCUMENT);
        }
        try {
            JsonToken token = parser.nextToken();
            if (token == null) {
                parseCompleted = true;
                return success(JsonAtomType.END_OF_DOCUMENT);
            }
            currentToken = tokenBindings.forProviderToken(token);
            return success(currentToken);
        } catch (IOException e) {
            return failure(e);
        }
    }

    public static Result<JsonExtractor>
    extractorFor(String jsonString, JsonTokenBindings tokenBindings) {
        Result<JsonParser> result = getStreamParser(jsonString);
        if (result.failed()) {
            return failure(result.getErrorMessage());
        }
        JacksonJsonExtractor jacksonParser = new JacksonJsonExtractor(
            result.output, tokenBindings);
        return success(jacksonParser);

    }

    private static Result<JsonParser> getStreamParser(
        String jsonString) {
        JsonFactory factory = new JsonFactory();
        try {
            JsonParser parser = factory.createParser(jsonString);
            return success(parser);
        } catch (Exception e) {
            return failure(e);
        }
    }

    private Result<JsonAtom> constructAtom(JsonAtomType currentToken) {
        if (currentToken == FIELD_NAME) {
            String fieldName;
            try {
                fieldName = parser.getCurrentName();
                return makeAtom(FIELD_NAME, String.class,
                    fieldName);
            } catch (IOException e) {
                return failure(e.getLocalizedMessage());
            }
        }
        try {
            switch (currentToken) {
                case STRING:
                    return makeAtom(currentToken, String.class,
                        parser.getValueAsString());
                case LONG:
                    return makeAtom(currentToken, Long.class,
                        parser.getValueAsLong());
                case DOUBLE:
                    return makeAtom(currentToken, Double.class,
                        parser.getValueAsDouble());
                case TRUE:
                    return makeAtom(TRUE, Boolean.class, true);
                case FALSE:
                    return makeAtom(FALSE, Boolean.class, false);
                case NULL:
                    return success(EMPTY_ATOM);
                case NOT_A_JSON_TOKEN:
                    return makeAtom(NOT_A_JSON_TOKEN, String.class,
                        "Not a JSON token");
                case NO_TOKEN_PARSED_YET:
                    return makeAtom(NO_TOKEN_PARSED_YET, String.class,
                        "No token parsed yet");
            }
        } catch (Exception e) {
            return makeAtom(BAD, String.class, e.getLocalizedMessage());
        }
        return makeAtom(UNKNOWN, String.class, "Unrecognized token: " +
                currentToken.name()
        );
    }
}
