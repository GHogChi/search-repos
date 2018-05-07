package com.spenkana.exp.searchrepos.support.io.json;

import java.util.HashMap;
import java.util.Map;

import static com.spenkana.exp.searchrepos.support.io.json.JsonAtomType
    .NOT_A_JSON_TOKEN;

/**
 * Builds a static map of JsonStructureTokens by provider tokens.
 * @param <T> provider token type.
 */
public class JsonTokenBindings<T> {
    private final Map<T, JsonAtomType> bindings;

    public JsonTokenBindings(
        Map<T, JsonAtomType> bindings) {
        this.bindings = bindings;
    }

    public JsonAtomType forProviderToken(T providerToken){
        return bindings.containsKey(providerToken)
            ? bindings.get(providerToken) : NOT_A_JSON_TOKEN;
    }

    public static class Builder<T> {
        Map<T, JsonAtomType> nativeTokensByProviderToken = new
            HashMap<>();
        public Builder add(T providerToken, JsonAtomType nativeToken){
            nativeTokensByProviderToken.put(providerToken, nativeToken);
            return this;
        }
        public JsonTokenBindings<T> build(){
            return new JsonTokenBindings<T>
                (nativeTokensByProviderToken);
        }
    }
}
