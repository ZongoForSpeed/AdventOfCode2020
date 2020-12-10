package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day01Test {
    public static long reportRepair2(long[] report) {
        for (int i = 0; i < report.length; i++) {
            for (int j = i + 1; j < report.length; j++) {
                if (report[i] + report[j] == 2020) {
                    return report[i] * report[j];
                }
            }
        }

        return -1;
    }

    public static long reportRepair3(long[] report) {
        for (int i = 0; i < report.length; i++) {
            for (int j = i + 1; j < report.length; j++) {
                if (report[i] + report[j] > 2020) {
                    continue;
                }
                for (int k = j + 1; k < report.length; k++) {
                    if (report[i] + report[j] + report[k] == 2020) {
                        return report[i] * report[j] * report[k];
                    }
                }
            }
        }

        return -1;
    }


    @Test
    void testReportRepair() {
        long[] report = new long[] {
                1721L, 979L, 366L, 299L, 675L, 1456L
        };

        assertThat(reportRepair2(report)).isEqualTo(514579);
        assertThat(reportRepair3(report)).isEqualTo(241861950);

    }

    /**
     * --- Day 1: Report Repair ---
     * After saving Christmas five years in a row, you've decided to take a
     * vacation at a nice resort on a tropical island. Surely, Christmas will go
     * on without you.
     *
     * The tropical island has its own currency and is entirely cash-only. The
     * gold coins used there have a little picture of a starfish; the locals just
     * call them stars. None of the currency exchanges seem to have heard of them,
     * but somehow, you'll need to find fifty of these coins by the time you
     * arrive so you can pay the deposit on your room.
     *
     * To save your vacation, you need to get all fifty stars by December 25th.
     *
     * Collect stars by solving puzzles. Two puzzles will be made available on
     * each day in the Advent calendar; the second puzzle is unlocked when you
     * complete the first. Each puzzle grants one star. Good luck!
     *
     * Before you leave, the Elves in accounting just need you to fix your expense
     * report (your puzzle input); apparently, something isn't quite adding up.
     *
     * Specifically, they need you to find the two entries that sum to 2020 and
     * then multiply those two numbers together.
     *
     * For example, suppose your expense report contained the following:
     *
     * 1721
     * 979
     * 366
     * 299
     * 675
     * 1456
     *
     * In this list, the two entries that sum to 2020 are 1721 and 299.
     * Multiplying them together produces 1721 * 299 = 514579, so the correct
     * answer is 514579.
     *
     * Of course, your expense report is much larger. Find the two entries that
     * sum to 2020; what do you get if you multiply them together?
     *
     * --- Part Two ---
     * The Elves in accounting are thankful for your help; one of them even offers
     * you a starfish coin they had left over from a past vacation. They offer you
     * a second one if you can find three numbers in your expense report that meet
     * the same criteria.
     *
     * Using the above example again, the three entries that sum to 2020 are 979,
     * 366, and 675. Multiplying them together produces the answer, 241861950.
     *
     * In your expense report, what is the product of the three entries that sum
     * to 2020?
     */
    @Test
    void inputReportRepair() throws IOException {
        List<String> input = FileUtils.readLines("/day/1/input");
        long[] report = input.stream().mapToLong(Long::valueOf).toArray();

        assertThat(reportRepair2(report)).isEqualTo(1016964);
        assertThat(reportRepair3(report)).isEqualTo(182588480);
    }

}
