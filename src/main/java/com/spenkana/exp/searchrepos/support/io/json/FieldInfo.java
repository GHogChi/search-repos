package com.spenkana.exp.searchrepos.support.io.json;

public class FieldInfo<T> extends JsonElementInfo {
    public final String fieldName;
    public final T value;

    public FieldInfo(String fieldName, T value, Path path) {
        super(path);
        this.fieldName = fieldName;
        this.value = value;
    }
}
