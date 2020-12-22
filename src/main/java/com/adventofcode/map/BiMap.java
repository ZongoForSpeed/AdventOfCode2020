package com.adventofcode.map;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class BiMap<K, V> {
    private final Map<K, V> map1 = new HashMap<>();
    private final Map<V, K> map2 = new HashMap<>();

    public void put(K key, V value) {
        map1.put(key, value);
        map2.put(value, key);
    }

    public V getValue(K key) {
        return map1.get(key);
    }

    public K getKey(V value) {
        return map2.get(value);
    }

    public boolean containsKey(K key) {
        return map1.containsKey(key);
    }

    public boolean containsValue(V value) {
        return map2.containsKey(value);
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner(", ","BiMap[", "]" );
        map1.forEach((k, v) -> stringJoiner.add(k + " <=> " + v));
        return stringJoiner.toString();
    }
}
