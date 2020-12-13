package com.adventofcode.maths;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArithmeticTest {

    private static final long[] PRIMES = new long[]{
            2, 3, 5, 7, 11, 13, 17, 19, 23,
            29, 31, 37, 41, 43, 47, 53, 59,
            61, 67, 71, 73, 79, 83, 89, 97
    };

    @Test
    void testPhi() {
        assertThat(Arithmetic.phi(3246999210L, PRIMES))
                .isEqualTo(640120320L);

        assertThat(Arithmetic.phi(496L, PRIMES))
                .isEqualTo(240);
    }

    @Test
    void testPowerMod() {
        assertThat(Arithmetic.powerMod(2, 10, 100))
                .isEqualTo(24);

        assertThat(Arithmetic.powerMod(97643, 276799, 456753))
                .isEqualTo(368123);
    }

    @Test
    void testModularInverse() {
        assertThat(Arithmetic.modularInverse(3, 11, PRIMES))
                .isEqualTo(4);

        assertThat(Arithmetic.modularInverse(97643, 456753, PRIMES))
                .isEqualTo(368123);

        assertThat(Arithmetic.modularInverse(107113, 3246999210L, PRIMES))
                .isEqualTo(180730717L);
    }

    @Test
    void testCRT() {
        long crt1 = Arithmetic.chineseRemainderTheorem(new long[]{3, 5, 7}, new long[]{2, 3, 2}, PRIMES);
        assertThat(crt1).isEqualTo(23);

        long crt2 = Arithmetic.chineseRemainderTheorem(new long[]{3, 4, 5}, new long[]{2, 3, 1}, PRIMES);
        assertThat(crt2).isEqualTo(11);
    }
}