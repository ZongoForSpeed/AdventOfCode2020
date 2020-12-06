package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day06Test {
    private static long countUniqueQuestions(List<String> group) {
        return group.stream().flatMapToInt(String::chars).distinct().count();
    }

    private static long countAllResponded(List<String> group) {
        int size = group.size();
        long[] questions = new long[255];
        group.stream()
                .flatMapToInt(s -> s.chars().distinct())
                .forEach(i -> questions[i]++);

        return Arrays.stream(questions).filter(q -> q == size).count();
    }

    private static List<List<String>> readGroups(List<String> batch) {
        List<List<String>> groups = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();

        for (String line : batch) {
            if (line.isEmpty()) {
                groups.add(currentGroup);
                currentGroup = new ArrayList<>();
            } else {
                currentGroup.add(line);
            }
        }
        groups.add(currentGroup);
        return groups;
    }

    @Test
    void testCountUniqueQuestions() {
        List<String> group = List.of("abcx",
                "abcy",
                "abcz");

        assertThat(countUniqueQuestions(group)).isEqualTo(6);

        List<String> batch = List.of("abc",
                "",
                "a",
                "b",
                "c",
                "",
                "ab",
                "ac",
                "",
                "a",
                "a",
                "a",
                "a",
                "",
                "b");

        long sum = readGroups(batch).stream().map(Day06Test::countUniqueQuestions).mapToLong(t -> t).sum();
        assertThat(sum).isEqualTo(11);
    }

    @Test
    void testCountAllResponded() {
        List<String> batch = List.of("abc",
                "",
                "a",
                "b",
                "c",
                "",
                "ab",
                "ac",
                "",
                "a",
                "a",
                "a",
                "a",
                "",
                "b");

        long sum = readGroups(batch).stream().map(Day06Test::countAllResponded).mapToLong(t -> t).sum();
        assertThat(sum).isEqualTo(6);
    }

    /**
     * --- Day 6: Custom Customs ---
     * As your flight approaches the regional airport where you'll switch to a
     * much larger plane, customs declaration forms are distributed to the
     * passengers.
     * <p>
     * The form asks a series of 26 yes-or-no questions marked a through z. All
     * you need to do is identify the questions for which anyone in your group
     * answers "yes". Since your group is just you, this doesn't take very long.
     * <p>
     * However, the person sitting next to you seems to be experiencing a language
     * barrier and asks if you can help. For each of the people in their group,
     * you write down the questions for which they answer "yes", one per line. For
     * example:
     * <p>
     * abcx
     * abcy
     * abcz
     * <p>
     * In this group, there are 6 questions to which anyone answered "yes": a, b,
     * c, x, y, and z. (Duplicate answers to the same question don't count extra;
     * each question counts at most once.)
     * <p>
     * Another group asks for your help, then another, and eventually you've
     * collected answers from every group on the plane (your puzzle input). Each
     * group's answers are separated by a blank line, and within each group, each
     * person's answers are on a single line. For example:
     * <p>
     * abc
     * <p>
     * a
     * b
     * c
     * <p>
     * ab
     * ac
     * <p>
     * a
     * a
     * a
     * a
     * <p>
     * b
     * <p>
     * This list represents answers from five groups:
     * <p>
     * -   The first group contains one person who answered "yes" to 3 questions:
     * a, b, and c.
     * -   The second group contains three people; combined, they answered "yes"
     * to 3 questions: a, b, and c.
     * -   The third group contains two people; combined, they answered "yes" to
     * 3 questions: a, b, and c.
     * -   The fourth group contains four people; combined, they answered "yes"
     * to only 1 question, a.
     * -   The last group contains one person who answered "yes" to only 1
     * question, b.
     * <p>
     * In this example, the sum of these counts is 3 + 3 + 3 + 1 + 1 = 11.
     * <p>
     * For each group, count the number of questions to which anyone answered
     * "yes". What is the sum of those counts?
     * <p>
     * --- Part Two ---
     * <p>
     * As you finish the last group's customs declaration, you notice that you
     * misread one word in the instructions:
     * <p>
     * You don't need to identify the questions to which anyone answered "yes";
     * you need to identify the questions to which everyone answered "yes"!
     * <p>
     * Using the same example as above:
     * <p>
     * abc
     * <p>
     * a
     * b
     * c
     * <p>
     * ab
     * ac
     * <p>
     * a
     * a
     * a
     * a
     * <p>
     * b
     * <p>
     * This list represents answers from five groups:
     * <p>
     * -   In the first group, everyone (all 1 person) answered "yes" to 3
     * questions: a, b, and c.
     * -   In the second group, there is no question to which everyone answered
     * "yes".
     * -   In the third group, everyone answered yes to only 1 question, a. Since
     * some people did not answer "yes" to b or c, they don't count.
     * -   In the fourth group, everyone answered yes to only 1 question, a.
     * -   In the fifth group, everyone (all 1 person) answered "yes" to 1
     * question, b.
     * <p>
     * In this example, the sum of these counts is 3 + 0 + 1 + 1 + 1 = 6.
     * <p>
     * For each group, count the number of questions to which everyone answered
     * "yes". What is the sum of those counts?
     */
    @Test
    void inputCustomCustoms() throws IOException {
        List<List<String>> groups = readGroups(FileUtils.readLines("/day/6/input"));
        long countUniqueQuestions = groups.stream().map(Day06Test::countUniqueQuestions).mapToLong(t -> t).sum();
        assertThat(countUniqueQuestions).isEqualTo(6799);

        long countAllResponded = groups.stream().map(Day06Test::countAllResponded).mapToLong(t -> t).sum();
        assertThat(countAllResponded).isEqualTo(3354);
    }
}
