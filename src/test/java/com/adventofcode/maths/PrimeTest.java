package com.adventofcode.maths;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PrimeTest {

    @Test
    void testSieve() {
        long[] primes = Prime.sieve(100);
        assertThat(primes)
                .containsExactly(2, 3, 5, 7, 11, 13, 17, 19, 23,
                        29, 31, 37, 41, 43, 47, 53, 59,
                        61, 67, 71, 73, 79, 83, 89, 97);
    }


    @Test
    void testSieve2() {
        long[] primes = Prime.sieve2(100);
        assertThat(primes)
                .containsExactly(2, 3, 5, 7, 11, 13, 17, 19, 23,
                        29, 31, 37, 41, 43, 47, 53, 59,
                        61, 67, 71, 73, 79, 83, 89, 97);
    }
}