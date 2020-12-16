package com.adventofcode.utils;

import java.util.Arrays;
import java.util.BitSet;

public class IntMemory {
    private final BitSet bitSet;
    private int[] mem;

    public IntMemory() {
        this(10);
    }

    public IntMemory(int capacity) {
        mem = new int[capacity];
        bitSet = new BitSet(10);
    }

    public Integer get(int key) {
        if (key < mem.length && bitSet.get(key)) {
            return mem[key];
        }

        return null;
    }

    public Integer put(int key, int value) {
        if (key >= mem.length) {
            grow(key);
        }

        Integer last = get(key);
        mem[key] = value;
        bitSet.set(key);
        return last;
    }

    private void grow(int minCapacity) {
        int oldLength = mem.length;
        int newLength = Math.max(oldLength * 2, minCapacity + 1);

        mem = Arrays.copyOf(mem, newLength);
    }
}
