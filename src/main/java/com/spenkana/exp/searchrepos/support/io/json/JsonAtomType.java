package com.spenkana.exp.searchrepos.support.io.json;

/**
 * Numeric types are handled in their widest forms.
 */
public enum JsonAtomType {
    NO_TOKEN_PARSED_YET,
    EMPTY, BAD, UNKNOWN,
    START_ARRAY, END_ARRAY, START_OBJECT, END_OBJECT,
    NOT_A_JSON_TOKEN, FIELD_NAME,
    STRING, LONG, DOUBLE, TRUE, FALSE, NULL,
    END_OF_DOCUMENT
}
