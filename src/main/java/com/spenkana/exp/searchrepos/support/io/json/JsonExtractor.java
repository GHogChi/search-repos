package com.spenkana.exp.searchrepos.support.io.json;

import com.spenkana.exp.searchrepos.support.SafeSupplier;
import com.spenkana.exp.searchrepos.support.result.Result;

import java.util.function.Supplier;

/**
 * Wraps a third-party parser as a pull-driven parser.
 */
public abstract class JsonExtractor implements SafeSupplier<JsonAtom> {

    /**
     * Ratchet one type-token forward
     * @return
     */
    public abstract Result<JsonAtomType> nextAtomType();


}
