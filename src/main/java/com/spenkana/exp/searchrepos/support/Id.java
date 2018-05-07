package com.spenkana.exp.searchrepos.support;

import java.util.Objects;

public abstract class Id<T> {
    public final T id;

    protected Id(T id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Id)) return false;
        Id<?> id1 = (Id<?>) o;
        return Objects.equals(id, id1.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
