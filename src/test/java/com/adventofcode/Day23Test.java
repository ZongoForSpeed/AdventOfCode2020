package com.adventofcode;

import com.adventofcode.memory.ObjectMemory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Day23Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day23Test.class);

    private static String crabCupsPart1(String input, int moves) {
        List<Integer> cups = new ArrayList<>();
        for (char c : input.toCharArray()) {
            cups.add(Character.getNumericValue(c));
        }
        CupCircle cupCircle = new CupCircle(cups.stream().mapToInt(t -> t).toArray());
        for (int move = 1; move <= moves; ++move) {
            cupCircle.performStep();
        }
        LOGGER.info("Result crabCupsPart2({}, {}): {}", input, moves, cupCircle.resultPart1());
        return cupCircle.resultPart1();
    }

    private static long crabCupsPart2(String input, int moves, int size) {
        List<Integer> cups = new ArrayList<>();
        for (char c : input.toCharArray()) {
            cups.add(Character.getNumericValue(c));
        }
        while (cups.size() < size) {
            cups.add(cups.size() + 1);
        }
        CupCircle cupCircle = new CupCircle(cups.stream().mapToInt(t -> t).toArray());
        for (int move = 1; move <= moves; ++move) {
            cupCircle.performStep();
        }
        LOGGER.info("Result crabCupsPart2({}, {}, {}): {}", input, moves, size, cupCircle.resultPart2());
        return cupCircle.resultPart2();
    }

    @Test
    void testCrabCups() {
        String input = "389125467";
        assertThat(crabCupsPart1(input, 10)).isEqualTo("92658374");
        assertThat(crabCupsPart1(input, 100)).isEqualTo("67384529");

        assertThat(crabCupsPart2(input, 10_000_000, 1_000_000)).isEqualTo(149245887792L);
    }

    /**
     * --- Day 23: Crab Cups ---
     * The small crab challenges you to a game! The crab is going to mix up some
     * cups, and you have to predict where they'll end up.
     * <p>
     * The cups will be arranged in a circle and labeled clockwise (your puzzle
     * input). For example, if your labeling were 32415, there would be five cups
     * in the circle; going clockwise around the circle from the first cup, the
     * cups would be labeled 3, 2, 4, 1, 5, and then back to 3 again.
     * <p>
     * Before the crab starts, it will designate the first cup in your list as the
     * current cup. The crab is then going to do 100 moves.
     * <p>
     * Each move, the crab does the following actions:
     * <p>
     * - The crab picks up the three cups that are immediately clockwise of the
     * current cup. They are removed from the circle; cup spacing is adjusted
     * as necessary to maintain the circle.
     * - The crab selects a destination cup: the cup with a label equal to the
     * current cup's label minus one. If this would select one of the cups
     * that was just picked up, the crab will keep subtracting one until it
     * finds a cup that wasn't just picked up. If at any point in this
     * process the value goes below the lowest value on any cup's label, it
     * wraps around to the highest value on any cup's label instead.
     * - The crab places the cups it just picked up so that they are
     * immediately clockwise of the destination cup. They keep the same order
     * as when they were picked up.
     * - The crab selects a new current cup: the cup which is immediately
     * clockwise of the current cup.
     * <p>
     * For example, suppose your cup labeling were 389125467. If the crab were to
     * do merely 10 moves, the following changes would occur:
     * <p>
     * -- move 1 --
     * cups: (3) 8  9  1  2  5  4  6  7
     * pick up: 8, 9, 1
     * destination: 2
     * <p>
     * -- move 2 --
     * cups:  3 (2) 8  9  1  5  4  6  7
     * pick up: 8, 9, 1
     * destination: 7
     * <p>
     * -- move 3 --
     * cups:  3  2 (5) 4  6  7  8  9  1
     * pick up: 4, 6, 7
     * destination: 3
     * <p>
     * -- move 4 --
     * cups:  7  2  5 (8) 9  1  3  4  6
     * pick up: 9, 1, 3
     * destination: 7
     * <p>
     * -- move 5 --
     * cups:  3  2  5  8 (4) 6  7  9  1
     * pick up: 6, 7, 9
     * destination: 3
     * <p>
     * -- move 6 --
     * cups:  9  2  5  8  4 (1) 3  6  7
     * pick up: 3, 6, 7
     * destination: 9
     * <p>
     * -- move 7 --
     * cups:  7  2  5  8  4  1 (9) 3  6
     * pick up: 3, 6, 7
     * destination: 8
     * <p>
     * -- move 8 --
     * cups:  8  3  6  7  4  1  9 (2) 5
     * pick up: 5, 8, 3
     * destination: 1
     * <p>
     * -- move 9 --
     * cups:  7  4  1  5  8  3  9  2 (6)
     * pick up: 7, 4, 1
     * destination: 5
     * <p>
     * -- move 10 --
     * cups: (5) 7  4  1  8  3  9  2  6
     * pick up: 7, 4, 1
     * destination: 3
     * <p>
     * -- final --
     * cups:  5 (8) 3  7  4  1  9  2  6
     * <p>
     * In the above example, the cups' values are the labels as they appear moving
     * clockwise around the circle; the current cup is marked with ( ).
     * <p>
     * After the crab is done, what order will the cups be in? Starting after the
     * cup labeled 1, collect the other cups' labels clockwise into a single
     * string with no extra characters; each number except 1 should appear exactly
     * once. In the above example, after 10 moves, the cups clockwise from 1 are
     * labeled 9, 2, 6, 5, and so on, producing 92658374. If the crab were to
     * complete all 100 moves, the order after cup 1 would be 67384529.
     * <p>
     * Using your labeling, simulate 100 moves. What are the labels on the cups
     * after cup 1?
     * <p>
     * --- Part Two ---
     * <p>
     * Due to what you can only assume is a mistranslation (you're not exactly
     * fluent in Crab), you are quite surprised when the crab starts arranging
     * many cups in a circle on your raft - one million (1000000) in total.
     * <p>
     * Your labeling is still correct for the first few cups; after that, the
     * remaining cups are just numbered in an increasing fashion starting from
     * the number after the highest number in your list and proceeding one by one
     * until one million is reached. (For example, if your labeling were 54321,
     * the cups would be numbered 5, 4, 3, 2, 1, and then start counting up from 6
     * until one million is reached.) In this way, every number from one through
     * one million is used exactly once.
     * <p>
     * After discovering where you made the mistake in translating Crab Numbers,
     * you realize the small crab isn't going to do merely 100 moves; the crab is
     * going to do ten million (10000000) moves!
     * <p>
     * The crab is going to hide your stars - one each - under the two cups that
     * will end up immediately clockwise of cup 1. You can have them if you
     * predict what the labels on those cups will be when the crab is finished.
     * <p>
     * In the above example (389125467), this would be 934001 and then 159792;
     * multiplying these together produces 149245887792.
     * <p>
     * Determine which two cups will end up immediately clockwise of cup 1. What
     * do you get if you multiply their labels together?
     */
    @Test
    void inputCrabCups() {
        String input = "789465123";
        assertThat(crabCupsPart1(input, 100)).isEqualTo("98752463");

        assertThat(crabCupsPart2(input, 10_000_000, 1_000_000)).isEqualTo(2000455861L);
    }

    private static class Cup {
        private final int id;
        private Cup next = null;

        public Cup(int id) {
            this.id = id;
        }

        public int id() {
            return id;
        }

        public Cup next() {
            return next;
        }

        public void next(Cup next) {
            this.next = next;
        }
    }

    private static class CupCircle {
        private final ObjectMemory<Cup> cups;
        private final int min;
        private final int max;
        private Cup head;

        public CupCircle(int[] cupIndexes) {
            max = cupIndexes.length;
            min = 1;

            cups = new ObjectMemory<>(cupIndexes.length + 1);
            for (int cupIndex : cupIndexes) {
                cups.put(cupIndex, new Cup(cupIndex));
            }

            for (int i = 0, cupIndexesLength = cupIndexes.length - 1; i < cupIndexesLength; i++) {
                cups.get(cupIndexes[i]).next(cups.get(cupIndexes[i + 1]));
            }

            head = cups.get(cupIndexes[0]);
            cups.get(cupIndexes[cupIndexes.length - 1]).next(head);
        }

        public void performStep() {
            Set<Integer> forbidden = new HashSet<>();
            Cup moveStart = head.next();
            forbidden.add(moveStart.id());

            Cup moveEnd = moveStart;
            for (long i = 0; i < 2; i++) {
                moveEnd = moveEnd.next();
                forbidden.add(moveEnd.id());
            }
            head.next(moveEnd.next());

            int destination = head.id();
            do {
                destination -= 1;
                if (destination < min)
                    destination = max;
            } while (forbidden.contains(destination));

            Cup destinationCup = cups.get(destination);

            moveEnd.next(destinationCup.next());
            destinationCup.next(moveStart);
            head = head.next();
        }

        public String resultPart1() {
            StringBuilder stringBuilder = new StringBuilder();
            for (Cup current = cups.get(1).next(); current.id() != 1; current = current.next()) {
                stringBuilder.append(current.id());
            }
            return stringBuilder.toString();
        }

        public long resultPart2() {
            long result = 1;
            Cup current = cups.get(1).next();
            result *= current.id();
            current = current.next();
            result *= current.id();
            return result;
        }
    }
}
