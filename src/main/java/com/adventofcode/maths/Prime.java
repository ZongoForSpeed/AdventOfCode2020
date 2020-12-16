package com.adventofcode.maths;

import java.util.BitSet;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public final class Prime {
    private Prime() {

    }

    public static long[] sieve(int size) {
        BitSet test = new BitSet(size);
        test.set(0, size - 1, true);
        for (int p = 2; p * p < size; ++p) {
            if (test.get(p)) {
                for (int k = p * p; k < size; k += p) {
                    test.set(k, false);
                }
            }
        }

        return IntStream.range(2, size)
                .filter(test::get)
                .mapToLong(t -> t)
                .toArray();
    }

    public static long[] sieve2(int size) {
        BitSet test = new BitSet(size / 2);
        test.set(0, false);
        test.set(1, size - 1, true);
        for (int p = 1; p * p < size / 4; ++p) {
            if (test.get(p)) {
                for (int k = 2 * (p * p + p); k < size / 2; k += 2 * p + 1) {
                    test.set(k, false);
                }
            }
        }

        return LongStream.concat(LongStream.of(2),
                IntStream.range(1, size / 2)
                        .filter(test::get)
                        .mapToLong(p -> 2 * p + 1))
                .toArray();
    }
}
