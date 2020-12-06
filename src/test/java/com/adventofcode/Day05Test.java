package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.OptionalLong;

import static org.assertj.core.api.Assertions.assertThat;

public class Day05Test {
    public static long binaryBoarding(String boardingPasses) {
        long row = 0;
        long column = 0;

        for (int i = 0; i < boardingPasses.length(); ++i) {
            switch (boardingPasses.charAt(i)) {
                case 'F':
                    row <<= 1;
                    break;
                case 'B':
                    row++;
                    row <<= 1;
                    break;
                case 'L':
                    column <<= 1;
                    break;
                case 'R':
                    column += 1;
                    column <<= 1;
                    break;
            }
        }

        row >>= 1;
        column >>= 1;

        return row * 8 + column;
    }

    private static OptionalLong findEmptySeat(long[] seatIds) {
        for (int i = 0; i < seatIds.length - 1; i++) {
            if (seatIds[i] + 2 == seatIds[i + 1]) {
                return OptionalLong.of(seatIds[i] + 1);
            }
        }

        return OptionalLong.empty();
    }

    @Test
    void testBinaryBoarding() {
        assertThat(binaryBoarding("FBFBBFFRLR")).isEqualTo(357);
        assertThat(binaryBoarding("BFFFBBFRRR")).isEqualTo(567);
        assertThat(binaryBoarding("FFFBBBFRRR")).isEqualTo(119);
        assertThat(binaryBoarding("BBFFBBFRLL")).isEqualTo(820);
    }

    /**
     * --- Day 5: Binary Boarding ---
     * You board your plane only to discover a new problem: you dropped your
     * boarding pass! You aren't sure which seat is yours, and all of the flight
     * attendants are busy with the flood of people that suddenly made it through
     * passport control.
     * <p>
     * You write a quick program to use your phone's camera to scan all of the
     * nearby boarding passes (your puzzle input); perhaps you can find your seat
     * through process of elimination.
     * <p>
     * Instead of zones or groups, this airline uses binary space partitioning to
     * seat people. A seat might be specified like FBFBBFFRLR, where F means
     * "front", B means "back", L means "left", and R means "right".
     * <p>
     * The first 7 characters will either be F or B; these specify exactly one of
     * the 128 rows on the plane (numbered 0 through 127). Each letter tells you
     * which half of a region the given seat is in. Start with the whole list of
     * rows; the first letter indicates whether the seat is in the front (0
     * through 63) or the back (64 through 127). The next letter indicates which
     * half of that region the seat is in, and so on until you're left with
     * exactly one row.
     * <p>
     * For example, consider just the first seven characters of FBFBBFFRLR:
     * <p>
     * -   Start by considering the whole range, rows 0 through 127.
     * -   F means to take the lower half, keeping rows 0 through 63.
     * -   B means to take the upper half, keeping rows 32 through 63.
     * -   F means to take the lower half, keeping rows 32 through 47.
     * -   B means to take the upper half, keeping rows 40 through 47.
     * -   B keeps rows 44 through 47.
     * -   F keeps rows 44 through 45.
     * -   The final F keeps the lower of the two, row 44.
     * <p>
     * The last three characters will be either L or R; these specify exactly one
     * of the 8 columns of seats on the plane (numbered 0 through 7). The same
     * process as above proceeds again, this time with only three steps. L means
     * to keep the lower half, while R means to keep the upper half.
     * <p>
     * For example, consider just the last 3 characters of FBFBBFFRLR:
     * <p>
     * -   Start by considering the whole range, columns 0 through 7.
     * -   R means to take the upper half, keeping columns 4 through 7.
     * -   L means to take the lower half, keeping columns 4 through 5.
     * -   The final R keeps the upper of the two, column 5.
     * <p>
     * So, decoding FBFBBFFRLR reveals that it is the seat at row 44, column 5.
     * <p>
     * Every seat also has a unique seat ID: multiply the row by 8, then add the
     * column. In this example, the seat has ID 44 * 8 + 5 = 357.
     * <p>
     * Here are some other boarding passes:
     * <p>
     * -   BFFFBBFRRR: row 70, column 7, seat ID 567.
     * -   FFFBBBFRRR: row 14, column 7, seat ID 119.
     * -   BBFFBBFRLL: row 102, column 4, seat ID 820.
     * <p>
     * As a sanity check, look through your list of boarding passes. What is the
     * highest seat ID on a boarding pass?
     * <p>
     * --- Part Two ---
     * Ding! The "fasten seat belt" signs have turned on. Time to find your seat.
     * <p>
     * It's a completely full flight, so your seat should be the only missing
     * boarding pass in your list. However, there's a catch: some of the seats at
     * the very front and back of the plane don't exist on this aircraft, so
     * they'll be missing from your list as well.
     * <p>
     * Your seat wasn't at the very front or back, though; the seats with IDs +1
     * and -1 from yours will be in your list.
     * <p>
     * What is the ID of your seat?
     */
    @Test
    void imputBinaryBoarding() throws IOException {
        long[] seatIds = FileUtils.readLines("/day/5/input")
                .stream()
                .map(Day05Test::binaryBoarding)
                .mapToLong(s -> s)
                .sorted().toArray();

        long max = Arrays.stream(seatIds).max().orElseThrow();
        assertThat(max).isEqualTo(933);

        long emptySeat = findEmptySeat(seatIds).orElseThrow();
        assertThat(emptySeat).isEqualTo(711);
    }
}
