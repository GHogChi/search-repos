package com.spenkana.exp.searchrepos.support;

import com.spenkana.exp.searchrepos.support.result.Result;

@FunctionalInterface
public interface SafeSupplier<T> {
    Result<T> safeGet();
}
