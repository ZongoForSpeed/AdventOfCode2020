package com.adventofcode.maths;

import java.math.BigInteger;
import java.util.BitSet;
import java.util.stream.IntStream;

public class Arithmetic {
    public static long chineseRemainderTheorem(long[] modulos, long[] reminders, long[] primes) {
        assert (modulos.length == reminders.length);

        long n = 1;
        for (long modulo : modulos) {
            n *= modulo;
        }

        long result = 0;
        for (int i = 0; i < reminders.length; i++) {
            long modulo = modulos[i];
            long reminder = reminders[i];

            long r = n / modulo;
            result += r * modularInverse(r, modulo, primes) * reminder;
            result %= n;
        }

        return result;

    }

    public static long modularInverse(long a, long n, long[] primes) {
        long phi = phi(n, primes);
        return powerMod(a, phi - 1, n);
    }

    public static long phi(long n, long[] primes) {
        long result = n;
        for (long p : primes) {
            if (p * p > n) {
                break;
            }
            if (n % p == 0) {
                result = result - result / p;
                while (n % p == 0) {
                    n /= p;
                }
            }
        }
        if (n > 1) {
            result = result - result / n;
        }
        return result;
    }

    public static long powerMod(long b, long e, long modulo) {
        return BigInteger.valueOf(b).modPow(BigInteger.valueOf(e), BigInteger.valueOf(modulo)).longValue();
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
}
