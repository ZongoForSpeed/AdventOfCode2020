package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Day10Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day10Test.class);

    private static long arrangements(long n, Map<Long, Long> cache) {
        if (n < 0) {
            return 0;
        } else if (n == 0) {
            return 1;
        }

        Long value = cache.get(n);
        if (value != null) {
            return value;
        }

        value = arrangements(n - 1, cache) + arrangements(n - 2, cache) + arrangements(n - 3, cache);
        cache.put(n, value);
        return value;
    }

    private static Pair<Integer, Integer> jolterAdapter(List<Long> adapters) {
        adapters = new ArrayList<>(adapters);
        Collections.sort(adapters);
        adapters.add(0, 0L);
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        int[] diffs = new int[10];

        for (int i = 1; i < adapters.size(); i++) {
            long diff = adapters.get(i) - adapters.get(i - 1);
            // LOGGER.info("{} diff {}-{}", diff, adapters.get(i), adapters.get(i - 1));
            diffs[(int) diff]++;
        }

        LOGGER.info("diffs = {}", diffs);
        return Pair.of(diffs[1], diffs[3]);
    }

    private static long adapterArrangements(List<Long> adapters) {
        adapters = new ArrayList<>(adapters);
        Collections.sort(adapters);
        adapters.add(0, 0L);
        adapters.add(adapters.get(adapters.size() - 1) + 3);

        StringBuilder diffs = new StringBuilder();

        for (int i = 1; i < adapters.size(); i++) {
            long diff = adapters.get(i) - adapters.get(i - 1);
            // LOGGER.info("{} diff {}-{}", diff, adapters.get(i), adapters.get(i - 1));
            diffs.append(diff);
        }

        String[] split = diffs.toString().split("3");

        Map<Long, Long> cache = new HashMap<>();

        // LOGGER.info("diffs = {} : {}", diffs, split);
        long result = 1;
        for (String s : split) {
            result *= arrangements(s.length(), cache);
        }
        return result;
    }

    @Test
    void testAdapterArray1() {
        List<Long> adapters = List.of(
                16L,
                10L,
                15L,
                5L,
                1L,
                11L,
                7L,
                19L,
                6L,
                12L,
                4L
        );

        Pair<Integer, Integer> adapter = jolterAdapter(adapters);
        assertThat(adapter.getLeft()).isEqualTo(7);
        assertThat(adapter.getRight()).isEqualTo(5);

        assertThat(adapterArrangements(adapters)).isEqualTo(8);
    }

    @Test
    void testAdapterArray2() {
        List<Long> adapters = List.of(
                28L,
                33L,
                18L,
                42L,
                31L,
                14L,
                46L,
                20L,
                48L,
                47L,
                24L,
                23L,
                49L,
                45L,
                19L,
                38L,
                39L,
                11L,
                1L,
                32L,
                25L,
                35L,
                8L,
                17L,
                7L,
                9L,
                4L,
                2L,
                34L,
                10L,
                3L
        );

        Pair<Integer, Integer> adapter = jolterAdapter(adapters);
        assertThat(adapter.getLeft()).isEqualTo(22);
        assertThat(adapter.getRight()).isEqualTo(10);

        assertThat(adapterArrangements(adapters)).isEqualTo(19208);
    }

    /**
     * --- Day 10: Adapter Array ---
     * Patched into the aircraft's data port, you discover weather forecasts of a
     * massive tropical storm. Before you can figure out whether it will impact
     * your vacation plans, however, your device suddenly turns off!
     * <p>
     * Its battery is dead.
     * <p>
     * You'll need to plug it in. There's only one problem: the charging outlet
     * near your seat produces the wrong number of jolts. Always prepared, you
     * make a list of all of the joltage adapters in your bag.
     * <p>
     * Each of your joltage adapters is rated for a specific output joltage (your
     * puzzle input). Any given adapter can take an input 1, 2, or 3 jolts lower
     * than its rating and still produce its rated output joltage.
     * <p>
     * In addition, your device has a built-in joltage adapter rated for 3 jolts
     * higher than the highest-rated adapter in your bag. (If your adapter list
     * were 3, 9, and 6, your device's built-in adapter would be rated for 12
     * jolts.)
     * <p>
     * Treat the charging outlet near your seat as having an effective joltage
     * rating of 0.
     * <p>
     * Since you have some time to kill, you might as well test all of your
     * adapters. Wouldn't want to get to your resort and realize you can't even
     * charge your device!
     * <p>
     * If you use every adapter in your bag at once, what is the distribution of
     * joltage differences between the charging outlet, the adapters, and your
     * device?
     * <p>
     * For example, suppose that in your bag, you have adapters with the following
     * joltage ratings:
     * <p>
     * 16
     * 10
     * 15
     * 5
     * 1
     * 11
     * 7
     * 19
     * 6
     * 12
     * 4
     * <p>
     * With these adapters, your device's built-in joltage adapter would be rated
     * for 19 + 3 = 22 jolts, 3 higher than the highest-rated adapter.
     * <p>
     * Because adapters can only connect to a source 1-3 jolts lower than its
     * rating, in order to use every adapter, you'd need to choose them like this:
     * <p>
     * - The charging outlet has an effective rating of 0 jolts, so the only
     * adapters that could connect to it directly would need to have a
     * joltage rating of 1, 2, or 3 jolts. Of these, only one you have is an
     * adapter rated 1 jolt (difference of 1).
     * - From your 1-jolt rated adapter, the only choice is your 4-jolt rated
     * adapter (difference of 3).
     * - From the 4-jolt rated adapter, the adapters rated 5, 6, or 7 are valid
     * choices. However, in order to not skip any adapters, you have to pick
     * the adapter rated 5 jolts (difference of 1).
     * - Similarly, the next choices would need to be the adapter rated 6 and
     * then the adapter rated 7 (with difference of 1 and 1).
     * - The only adapter that works with the 7-jolt rated adapter is the one
     * rated 10 jolts (difference of 3).
     * - From 10, the choices are 11 or 12; choose 11 (difference of 1) and
     * then 12 (difference of 1).
     * - After 12, only valid adapter has a rating of 15 (difference of 3),
     * then 16 (difference of 1), then 19 (difference of 3).
     * - Finally, your device's built-in adapter is always 3 higher than the
     * highest adapter, so its rating is 22 jolts (always a difference of 3).
     * <p>
     * In this example, when using every adapter, there are 7 differences of 1
     * jolt and 5 differences of 3 jolts.
     * <p>
     * Here is a larger example:
     * <p>
     * 28
     * 33
     * 18
     * 42
     * 31
     * 14
     * 46
     * 20
     * 48
     * 47
     * 24
     * 23
     * 49
     * 45
     * 19
     * 38
     * 39
     * 11
     * 1
     * 32
     * 25
     * 35
     * 8
     * 17
     * 7
     * 9
     * 4
     * 2
     * 34
     * 10
     * 3
     * <p>
     * In this larger example, in a chain that uses all of the adapters, there are
     * 22 differences of 1 jolt and 10 differences of 3 jolts.
     * <p>
     * Find a chain that uses all of your adapters to connect the charging outlet
     * to your device's built-in adapter and count the joltage differences between
     * the charging outlet, the adapters, and your device. What is the number of
     * 1-jolt differences multiplied by the number of 3-jolt differences?
     * <p>
     * --- Part Two ---
     * <p>
     * To completely determine whether you have enough adapters, you'll need to
     * figure out how many different ways they can be arranged. Every arrangement
     * needs to connect the charging outlet to your device. The previous rules
     * about when adapters can successfully connect still apply.
     * <p>
     * The first example above (the one that starts with 16, 10, 15) supports the
     * following arrangements:
     * <p>
     * (0), 1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 6, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 6, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 6, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 7, 10, 12, 15, 16, 19, (22)
     * <p>
     * (The charging outlet and your device's built-in adapter are shown in
     * parentheses.) Given the adapters from the first example, the total number
     * of arrangements that connect the charging outlet to your device is 8.
     * <p>
     * The second example above (the one that starts with 28, 33, 18) has many
     * arrangements. Here are a few:
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 48, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 47, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 48, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 46, 49, (52)
     * <p>
     * (0), 1, 2, 3, 4, 7, 8, 9, 10, 11, 14, 17, 18, 19, 20, 23, 24, 25, 28, 31,
     * 32, 33, 34, 35, 38, 39, 42, 45, 47, 48, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 46, 48, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 46, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 47, 48, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 47, 49, (52)
     * <p>
     * (0), 3, 4, 7, 10, 11, 14, 17, 20, 23, 25, 28, 31, 34, 35, 38, 39, 42, 45,
     * 48, 49, (52)
     * <p>
     * In total, this set of adapters can connect the charging outlet to your
     * device in 19208 distinct arrangements.
     * <p>
     * You glance back down at your bag and try to remember why you brought so
     * many adapters; there must be more than a trillion valid ways to arrange
     * them! Surely, there must be an efficient way to count the arrangements.
     * <p>
     * What is the total number of distinct ways you can arrange the adapters to
     * connect the charging outlet to your device?
     */
    @Test
    void inputAdapterArray() throws IOException {
        List<Long> adapters = FileUtils.readLines("/day/10/input")
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Pair<Integer, Integer> adapter = jolterAdapter(adapters);
        assertThat(adapter.getLeft()).isEqualTo(72);
        assertThat(adapter.getRight()).isEqualTo(31);

        long result = adapter.getLeft() * adapter.getRight();
        assertThat(result).isEqualTo(2232);

        assertThat(adapterArrangements(adapters)).isEqualTo(173625106649344L);
    }
}
