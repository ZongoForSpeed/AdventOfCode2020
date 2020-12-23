package com.adventofcode.memory;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.stream.Collectors;

public class ObjectMemory<V> implements Memory<V> {
    private final BitSet bitSet;
    private Object[] mem;

    public ObjectMemory() {
        this(10);
    }

    public ObjectMemory(int capacity) {
        mem = new Object[capacity];
        bitSet = new BitSet(10);
    }

    @Override
    public int size() {
        return bitSet.cardinality();
    }

    @Override
    public boolean isEmpty() {
        return bitSet.isEmpty();
    }

    @Override
    public boolean containsKey(int key) {
        if (key < 0) {
            throw new IllegalStateException("Negative key are not allowed: " + key);
        }

        return key < mem.length && bitSet.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(int key) {
        if (key < 0) {
            throw new IllegalStateException("Negative key are not allowed: " + key);
        }

        if (key < mem.length && bitSet.get(key)) {
            return (V) mem[key];
        }

        return null;
    }

    @Override
    public V put(int key, V value) {
        if (key < 0) {
            throw new IllegalStateException("Negative key are not allowed: " + key);
        }

        if (key >= mem.length) {
            grow(key);
        }

        V last = get(key);
        mem[key] = value;
        bitSet.set(key);
        return last;
    }

    @Override
    public void clear() {
        bitSet.clear();
        mem = new Object[10];
    }

    @Override
    public int[] keySet() {
        return bitSet.stream().toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        return bitSet.stream().mapToObj(i -> (V) mem[i]).collect(Collectors.toList());

    }

    private void grow(int minCapacity) {
        int oldLength = mem.length;
        int newLength = Math.max(oldLength * 2, minCapacity + 1);

        mem = Arrays.copyOf(mem, newLength);
    }
}
