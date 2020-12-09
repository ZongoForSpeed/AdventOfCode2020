package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day09Test {
    boolean isValid(List<Long> codes, int start, int end, long value) {
        for (int n = start; n < end; ++n) {
            for (int m = n + 1; m < end; ++m) {
                if (codes.get(m) + codes.get(n) == value) {
                    return true;
                }
            }
        }

        return false;
    }

    List<Long> findContiguousSet(List<Long> codes, long value) {
        long[] sum = new long[codes.size() + 1];
        sum[0] = 0;
        for (int i = 0; i < codes.size(); i++) {
            sum[i + 1] = sum[i] + codes.get(i);
        }

        for (int n = 0; n < sum.length; ++n) {
            for (int m = n + 1; m < sum.length; ++m) {
                long s = sum[m] - sum[n];
                if (s == value) {
                    return codes.subList(n, m);
                }
            }
        }

        return Collections.emptyList();
    }

    OptionalLong findEncodingError(List<Long> codes, int preamble) {
        for (int n = preamble; n < codes.size(); ++n) {
            if (!isValid(codes, n - preamble, n, codes.get(n))) {
                return OptionalLong.of(codes.get(n));
            }
        }

        return OptionalLong.empty();
    }

    @Test
    void testEncodingError() {
        List<Long> codes = List.of(
                35L,
                20L,
                15L,
                25L,
                47L,
                40L,
                62L,
                55L,
                65L,
                95L,
                102L,
                117L,
                150L,
                182L,
                127L,
                219L,
                299L,
                277L,
                309L,
                576L
        );

        OptionalLong encodingError = findEncodingError(codes, 5);
        assertThat(encodingError)
                .isPresent()
                .hasValue(127);

        List<Long> contiguousSet = findContiguousSet(codes, 127);
        assertThat(contiguousSet).containsExactly(15L, 25L, 47L, 40L);
        long weakness = Collections.max(contiguousSet) + Collections.min(contiguousSet);
        assertThat(weakness).isEqualTo(62);
    }

    /**
     * --- Day 9: Encoding Error ---
     * With your neighbor happily enjoying their video game, you turn your
     * attention to an open data port on the little screen in the seat in front of
     * you.
     * <p>
     * Though the port is non-standard, you manage to connect it to your computer
     * through the clever use of several paperclips. Upon connection, the port
     * outputs a series of numbers (your puzzle input).
     * <p>
     * The data appears to be encrypted with the eXchange-Masking Addition System
     * (XMAS) which, conveniently for you, is an old cypher with an important
     * weakness.
     * <p>
     * XMAS starts by transmitting a preamble of 25 numbers. After that, each
     * number you receive should be the sum of any two of the 25 immediately
     * previous numbers. The two numbers will have different values, and there
     * might be more than one such pair.
     * <p>
     * For example, suppose your preamble consists of the numbers 1 through 25 in
     * a random order. To be valid, the next number must be the sum of two of
     * those numbers:
     * <p>
     * - 26 would be a valid next number, as it could be 1 plus 25 (or many
     * other pairs, like 2 and 24).
     * - 49 would be a valid next number, as it is the sum of 24 and 25.
     * - 100 would not be valid; no two of the previous 25 numbers sum to 100.
     * - 50 would also not be valid; although 25 appears in the previous 25
     * numbers, the two numbers in the pair must be different.
     * <p>
     * Suppose the 26th number is 45, and the first number (no longer an option,
     * as it is more than 25 numbers ago) was 20. Now, for the next number to be
     * valid, there needs to be some pair of numbers among 1-19, 21-25, or 45 that
     * add up to it:
     * <p>
     * - 26 would still be a valid next number, as 1 and 25 are still within
     * the previous 25 numbers.
     * - 65 would not be valid, as no two of the available numbers sum to it.
     * - 64 and 66 would both be valid, as they are the result of 19+45 and
     * 21+45 respectively.
     * <p>
     * Here is a larger example which only considers the previous 5 numbers (and
     * has a preamble of length 5):
     * <p>
     * 35
     * 20
     * 15
     * 25
     * 47
     * 40
     * 62
     * 55
     * 65
     * 95
     * 102
     * 117
     * 150
     * 182
     * 127
     * 219
     * 299
     * 277
     * 309
     * 576
     * <p>
     * In this example, after the 5-number preamble, almost every number is the
     * sum of two of the previous 5 numbers; the only number that does not follow
     * this rule is 127.
     * <p>
     * The first step of attacking the weakness in the XMAS data is to find the
     * first number in the list (after the preamble) which is not the sum of two
     * of the 25 numbers before it. What is the first number that does not have
     * this property?
     * <p>
     * --- Part Two ---
     * <p>
     * The final step in breaking the XMAS encryption relies on the invalid number
     * you just found: you must find a contiguous set of at least two numbers in
     * your list which sum to the invalid number from step 1.
     * <p>
     * Again consider the above example:
     * <p>
     * 35
     * 20
     * 15
     * 25
     * 47
     * 40
     * 62
     * 55
     * 65
     * 95
     * 102
     * 117
     * 150
     * 182
     * 127
     * 219
     * 299
     * 277
     * 309
     * 576
     * <p>
     * In this list, adding up all of the numbers from 15 through 40 produces the
     * invalid number from step 1, 127. (Of course, the contiguous set of numbers
     * in your actual list might be much longer.)
     * <p>
     * To find the encryption weakness, add together the smallest and largest
     * number in this contiguous range; in this example, these are 15 and 47,
     * producing 62.
     * <p>
     * What is the encryption weakness in your XMAS-encrypted list of numbers?
     */
    @Test
    void inputEncodingError() throws IOException {
        List<Long> codes = FileUtils.readLines("/day/9/input")
                .stream().map(Long::parseLong)
                .collect(Collectors.toList());

        OptionalLong encodingError = findEncodingError(codes, 25);
        assertThat(encodingError)
                .isPresent()
                .hasValue(3199139634L);

        List<Long> contiguousSet = findContiguousSet(codes, encodingError.getAsLong());
        assertThat(contiguousSet).containsExactly(
                122504099L,
                114441245L,
                143793543L,
                205448799L,
                209677440L,
                134686885L,
                168119071L,
                212819308L,
                262089204L,
                145005770L,
                141254137L,
                324118685L,
                155188637L,
                212067968L,
                311912614L,
                167400001L,
                168612228L);
        long weakness = Collections.max(contiguousSet) + Collections.min(contiguousSet);
        assertThat(weakness).isEqualTo(438559930L);
    }
}
