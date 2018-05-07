package com.spenkana.exp.searchrepos.support.io.json;

public class StructureInfo extends JsonElementInfo {
    public enum Structural {START, END, STANDALONE}
    public final JsonAtomType token;
    public final Structural type;

    public StructureInfo(Path path, JsonAtomType token,
                         Structural type) {
        super(path);
        this.token = token;
        this.type = type;
    }
}
