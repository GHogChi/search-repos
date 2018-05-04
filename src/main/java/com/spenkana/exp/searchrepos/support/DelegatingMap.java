package com.spenkana.exp.searchrepos.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A lot of work just to delegate and provide a simple name.
 * At least the IDE could help.
 */
class DelegatingMap<K,V> implements Map<K, V> {
    private final Map<K,V> delegatee = new HashMap<>();

    @Override
    public int size() {
        return delegatee.size();
    }

    @Override
    public boolean isEmpty() {
        return delegatee.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegatee.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegatee.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return delegatee.get(key);
    }

    @Override
    public V put(K key, V value) {
        return delegatee.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return delegatee.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegatee.putAll(m);
    }

    @Override
    public void clear() {
        delegatee.clear();
    }

    @Override
    public Set<K> keySet() {
        return delegatee.keySet();
    }

    @Override
    public Collection<V> values() {
        return delegatee.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegatee.entrySet();
    }

}
