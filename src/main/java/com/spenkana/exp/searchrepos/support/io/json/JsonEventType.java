package com.spenkana.exp.searchrepos.support.io.json;

import java.lang.reflect.Type;

public enum JsonEventType {
    FIELD_INFO(FieldInfo.class),
    STRUCTURE_INFO(StructureInfo.class);

    JsonEventType(Type type) {

    }
}
