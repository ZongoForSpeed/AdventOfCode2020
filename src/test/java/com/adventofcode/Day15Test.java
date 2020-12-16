package com.adventofcode;

import com.adventofcode.utils.IntMemory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class Day15Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day15Test.class);

    long memoryGame(String input, int turn) {
        IntMemory lastMemory = new IntMemory();
        IntMemory previousMemory = new IntMemory();

        int[] startingNumbers = Arrays.stream(input.split(",")).mapToInt(Integer::parseInt).toArray();
        int n = 1;
        int lastNumberSpoken = -1;
        boolean newNumber = false;
        for (; n <= startingNumbers.length; ++n) {
            lastNumberSpoken = startingNumbers[n - 1];

            // LOGGER.info("Turn {}, spoken number {}", n, lastNumberSpoken);

            Integer last = lastMemory.put(lastNumberSpoken, n);
            newNumber = last == null;
            if (last != null) {
                previousMemory.put(lastNumberSpoken, last);
            }
        }
        for (; n <= turn; ++n) {
            if (newNumber) {
                lastNumberSpoken = 0;
            } else {
                lastNumberSpoken = lastMemory.get(lastNumberSpoken) - previousMemory.get(lastNumberSpoken);
            }

            // LOGGER.info("Turn {}, spoken number {}", n, lastNumberSpoken);

            Integer last = lastMemory.put(lastNumberSpoken, n);
            newNumber = last == null;
            if (last != null) {
                previousMemory.put(lastNumberSpoken, last);
            }
        }

        return lastNumberSpoken;
    }

    @Test
    void testMemoryGame() {
        assertThat(memoryGame("0,3,6", 2020)).isEqualTo(436);
        assertThat(memoryGame("1,3,2", 2020)).isEqualTo(1);
        assertThat(memoryGame("2,1,3", 2020)).isEqualTo(10);
        assertThat(memoryGame("1,2,3", 2020)).isEqualTo(27);
        assertThat(memoryGame("2,3,1", 2020)).isEqualTo(78);
        assertThat(memoryGame("3,2,1", 2020)).isEqualTo(438);
        assertThat(memoryGame("3,1,2", 2020)).isEqualTo(1836);

        assertThat(memoryGame("0,3,6", 30000000)).isEqualTo(175594);
        assertThat(memoryGame("1,3,2", 30000000)).isEqualTo(2578);
        assertThat(memoryGame("2,1,3", 30000000)).isEqualTo(3544142);
        assertThat(memoryGame("1,2,3", 30000000)).isEqualTo(261214);
        assertThat(memoryGame("2,3,1", 30000000)).isEqualTo(6895259);
        assertThat(memoryGame("3,2,1", 30000000)).isEqualTo(18);
        assertThat(memoryGame("3,1,2", 30000000)).isEqualTo(362);
    }

    /**
     * --- Day 15: Rambunctious Recitation ---
     * <p>
     * You catch the airport shuttle and try to book a new flight to your vacation
     * island. Due to the storm, all direct flights have been cancelled, but a
     * route is available to get around the storm. You take it.
     * <p>
     * While you wait for your flight, you decide to check in with the Elves back
     * at the North Pole. They're playing a memory game and are ever so excited to
     * explain the rules!
     * <p>
     * In this game, the players take turns saying numbers. They begin by taking
     * turns reading from a list of starting numbers (your puzzle input). Then,
     * each turn consists of considering the most recently spoken number:
     * <p>
     * - If that was the first time the number has been spoken, the current
     * player says 0.
     * - Otherwise, the number had been spoken before; the current player
     * announces how many turns apart the number is from when it was
     * previously spoken.
     * <p>
     * So, after the starting numbers, each turn results in that player speaking
     * aloud either 0 (if the last number is new) or an age (if the last number is
     * a repeat).
     * <p>
     * For example, suppose the starting numbers are 0,3,6:
     * <p>
     * - Turn 1: The 1st number spoken is a starting number, 0.
     * - Turn 2: The 2nd number spoken is a starting number, 3.
     * - Turn 3: The 3rd number spoken is a starting number, 6.
     * - Turn 4: Now, consider the last number spoken, 6. Since that was the
     * first time the number had been spoken, the 4th number spoken is 0.
     * - Turn 5: Next, again consider the last number spoken, 0. Since it had
     * been spoken before, the next number to speak is the difference between
     * the turn number when it was last spoken (the previous turn, 4) and the
     * turn number of the time it was most recently spoken before then (turn
     * 1). Thus, the 5th number spoken is 4 - 1, 3.
     * - Turn 6: The last number spoken, 3 had also been spoken before, most
     * recently on turns 5 and 2. So, the 6th number spoken is 5 - 2, 3.
     * - Turn 7: Since 3 was just spoken twice in a row, and the last two turns
     * are 1 turn apart, the 7th number spoken is 1.
     * - Turn 8: Since 1 is new, the 8th number spoken is 0.
     * - Turn 9: 0 was last spoken on turns 8 and 4, so the 9th number spoken
     * is the difference between them, 4.
     * - Turn 10: 4 is new, so the 10th number spoken is 0.
     * <p>
     * (The game ends when the Elves get sick of playing or dinner is ready,
     * whichever comes first.)
     * <p>
     * Their question for you is: what will be the 2020th number spoken? In the example above, the 2020th number spoken will be 436.
     * <p>
     * Here are a few more examples:
     * <p>
     * Given the starting numbers 1,3,2, the 2020th number spoken is 1.
     * Given the starting numbers 2,1,3, the 2020th number spoken is 10.
     * Given the starting numbers 1,2,3, the 2020th number spoken is 27.
     * Given the starting numbers 2,3,1, the 2020th number spoken is 78.
     * Given the starting numbers 3,2,1, the 2020th number spoken is 438.
     * Given the starting numbers 3,1,2, the 2020th number spoken is 1836.
     * Given your starting numbers, what will be the 2020th number spoken?
     * <p>
     * --- Part Two ---
     * <p>
     * Impressed, the Elves issue you a challenge: determine the 30000000th number spoken. For example, given the same starting numbers as above:
     * <p>
     * Given 0,3,6, the 30000000th number spoken is 175594.
     * Given 1,3,2, the 30000000th number spoken is 2578.
     * Given 2,1,3, the 30000000th number spoken is 3544142.
     * Given 1,2,3, the 30000000th number spoken is 261214.
     * Given 2,3,1, the 30000000th number spoken is 6895259.
     * Given 3,2,1, the 30000000th number spoken is 18.
     * Given 3,1,2, the 30000000th number spoken is 362.
     * Given your starting numbers, what will be the 30000000th number spoken?
     */
    @Test
    void inputMemoryGame() {
        assertThat(memoryGame("12,20,0,6,1,17,7", 2020)).isEqualTo(866);
        assertThat(memoryGame("12,20,0,6,1,17,7", 30000000)).isEqualTo(1437692);
    }

}
