package com.adventofcode.memory;

import java.util.Collection;
import java.util.Objects;
import java.util.function.IntFunction;

public interface Memory<V> {
    int size();

    boolean isEmpty();

    boolean containsKey(int key);

    V get(int key);

    V put(int key, V value);

    void clear();

    int[] keySet();

    Collection<V> values();

    default V getOrDefault(int key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key))
                ? v
                : defaultValue;
    }

    default V putIfAbsent(int key, V value) {
        V v = get(key);
        if (v == null) {
            v = put(key, value);
        }

        return v;
    }

    default boolean replace(int key, V oldValue, V newValue) {
        Object curValue = get(key);
        if (!Objects.equals(curValue, oldValue) ||
                (curValue == null && !containsKey(key))) {
            return false;
        }
        put(key, newValue);
        return true;
    }

    default V computeIfAbsent(int key,
                              IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        V v;
        if ((v = get(key)) == null) {
            V newValue;
            if ((newValue = mappingFunction.apply(key)) != null) {
                put(key, newValue);
                return newValue;
            }
        }

        return v;
    }
}
