package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class Day19Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day19Test.class);

    private static long matchRules(List<String> lines, boolean addLoopRules) {
        Map<Integer, Predicate<String>> rules = new HashMap<>();
        List<String> messages = new ArrayList<>();
        boolean readRule = true;
        for (String line : lines) {
            if (StringUtils.isBlank(line)) {
                readRule = false;
                continue;
            }

            if (readRule) {
                String[] s1 = line.split(": ");
                int name = Integer.parseInt(s1[0]);

                String rhs = s1[1];
                if (rhs.startsWith("\"")) {
                    String pattern = rhs.replace("\"", "");
                    rules.put(name, new SimpleRule(pattern));
                } else {
                    List<Predicate<String>> compositeRules = new ArrayList<>();
                    String[] s2 = rhs.split(" \\| ");
                    for (String s : s2) {
                        String[] s3 = s.split(" ");
                        int[] ruleNumbers = Arrays.stream(s3).mapToInt(Integer::parseInt).toArray();
                        compositeRules.add(new CompositeRule(rules, ruleNumbers));
                    }

                    rules.put(name, compositeRules.stream().reduce(Predicate::or).orElseThrow());
                }
            } else {
                messages.add(line);
            }
        }
        if (addLoopRules) {
            rules.put(8, new CompositeRule(rules, new int[]{42}).or(new CompositeRule(rules, new int[]{42, 8})));
            rules.put(11, new CompositeRule(rules, new int[]{42, 31}).or(new CompositeRule(rules, new int[]{42, 11, 31})));
        }
        LOGGER.info("Rules: {}", rules);
        LOGGER.info("Messages: {}", messages);
        Predicate<String> rule0 = rules.get(0);
        long count = 0;
        for (String message : messages) {
            boolean test = rule0.test(message);
            LOGGER.info("Test message '{}' : {}", message, test);
            if (test) {
                ++count;
            }
        }
        return count;
    }

    @Test
    void testMatchRules() {
        List<String> input = List.of("0: 4 1 5",
                "1: 2 3 | 3 2",
                "2: 4 4 | 5 5",
                "3: 4 5 | 5 4",
                "4: \"a\"",
                "5: \"b\"",
                "",
                "ababbb",
                "bababa",
                "abbbab",
                "aaabbb",
                "aaaabbb");

        assertThat(matchRules(input, false)).isEqualTo(2);
    }

    @Test
    void testMonsterMessages() {
        List<String> input = List.of("42: 9 14 | 10 1",
                "9: 14 27 | 1 26",
                "10: 23 14 | 28 1",
                "1: \"a\"",
                "11: 42 31",
                "5: 1 14 | 15 1",
                "19: 14 1 | 14 14",
                "12: 24 14 | 19 1",
                "16: 15 1 | 14 14",
                "31: 14 17 | 1 13",
                "6: 14 14 | 1 14",
                "2: 1 24 | 14 4",
                "0: 8 11",
                "13: 14 3 | 1 12",
                "15: 1 | 14",
                "17: 14 2 | 1 7",
                "23: 25 1 | 22 14",
                "28: 16 1",
                "4: 1 1",
                "20: 14 14 | 1 15",
                "3: 5 14 | 16 1",
                "27: 1 6 | 14 18",
                "14: \"b\"",
                "21: 14 1 | 1 14",
                "25: 1 1 | 1 14",
                "22: 14 14",
                "8: 42",
                "26: 14 22 | 1 20",
                "18: 15 15",
                "7: 14 5 | 1 21",
                "24: 14 1",
                "",
                "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa",
                "bbabbbbaabaabba",
                "babbbbaabbbbbabbbbbbaabaaabaaa",
                "aaabbbbbbaaaabaababaabababbabaaabbababababaaa",
                "bbbbbbbaaaabbbbaaabbabaaa",
                "bbbababbbbaaaaaaaabbababaaababaabab",
                "ababaaaaaabaaab",
                "ababaaaaabbbaba",
                "baabbaaaabbaaaababbaababb",
                "abbbbabbbbaaaababbbbbbaaaababb",
                "aaaaabbaabaaaaababaa",
                "aaaabbaaaabbaaa",
                "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa",
                "babaaabbbaaabaababbaabababaaab",
                "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba");

        assertThat(matchRules(input, false)).isEqualTo(3);
        assertThat(matchRules(input, true)).isEqualTo(12);
    }

    /**
     * --- Day 19: Monster Messages ---
     * You land in an airport surrounded by dense forest. As you walk to your
     * high-speed train, the Elves at the Mythical Information Bureau contact you
     * again. They think their satellite has collected an image of a sea monster!
     * Unfortunately, the connection to the satellite is having problems, and many
     * of the messages sent back from the satellite have been corrupted.
     * <p>
     * They sent you a list of the rules valid messages should obey and a list of
     * received messages they've collected so far (your puzzle input).
     * <p>
     * The rules for valid messages (the top part of your puzzle input) are
     * numbered and build upon each other. For example:
     * <p>
     * 0: 1 2
     * 1: "a"
     * 2: 1 3 | 3 1
     * 3: "b"
     * <p>
     * Some rules, like 3: "b", simply match a single character (in this case, b).
     * <p>
     * The remaining rules list the sub-rules that must be followed; for example,
     * the rule 0: 1 2 means that to match rule 0, the text being checked must
     * match rule 1, and the text after the part that matched rule 1 must then
     * match rule 2.
     * <p>
     * Some of the rules have multiple lists of sub-rules separated by a pipe (|).
     * This means that at least one list of sub-rules must match. (The ones that
     * match might be different each time the rule is encountered.) For example,
     * the rule 2: 1 3 | 3 1 means that to match rule 2, the text being checked
     * must match rule 1 followed by rule 3 or it must match rule 3 followed by
     * rule 1.
     * <p>
     * Fortunately, there are no loops in the rules, so the list of possible
     * matches will be finite. Since rule 1 matches a and rule 3 matches b, rule 2
     * matches either ab or ba. Therefore, rule 0 matches aab or aba.
     * <p>
     * Here's a more interesting example:
     * <p>
     * 0: 4 1 5
     * 1: 2 3 | 3 2
     * 2: 4 4 | 5 5
     * 3: 4 5 | 5 4
     * 4: "a"
     * 5: "b"
     * <p>
     * Here, because rule 4 matches a and rule 5 matches b, rule 2 matches two
     * letters that are the same (aa or bb), and rule 3 matches two letters that
     * are different (ab or ba).
     * <p>
     * Since rule 1 matches rules 2 and 3 once each in either order, it must match
     * two pairs of letters, one pair with matching letters and one pair with
     * different letters. This leaves eight possibilities: aaab, aaba, bbab, bbba,
     * abaa, abbb, baaa, or babb.
     * <p>
     * Rule 0, therefore, matches a (rule 4), then any of the eight options from
     * rule 1, then b (rule 5): aaaabb, aaabab, abbabb, abbbab, aabaab, aabbbb,
     * abaaab, or ababbb.
     * <p>
     * The received messages (the bottom part of your puzzle input) need to be
     * checked against the rules so you can determine which are valid and which
     * are corrupted. Including the rules and the messages together, this might
     * look like:
     * <p>
     * 0: 4 1 5
     * 1: 2 3 | 3 2
     * 2: 4 4 | 5 5
     * 3: 4 5 | 5 4
     * 4: "a"
     * 5: "b"
     * <p>
     * ababbb
     * bababa
     * abbbab
     * aaabbb
     * aaaabbb
     * <p>
     * Your goal is to determine the number of messages that completely match rule
     * 0. In the above example, ababbb and abbbab match, but bababa, aaabbb, and
     * aaaabbb do not, producing the answer 2. The whole message must match all of
     * rule 0; there can't be extra unmatched characters in the message. (For
     * example, aaaabbb might appear to match rule 0 above, but it has an extra
     * unmatched b on the end.)
     * <p>
     * How many messages completely match rule 0?
     * <p>
     * To begin, get your puzzle input.
     * <p>
     * --- Part Two ---
     * <p>
     * As you look over the list of messages, you realize your matching rules
     * aren't quite right. To fix them, completely replace rules 8: 42 and
     * 11: 42 31 with the following:
     * <p>
     * 8: 42 | 42 8
     * 11: 42 31 | 42 11 31
     * <p>
     * This small change has a big impact: now, the rules do contain loops, and
     * the list of messages they could hypothetically match is infinite. You'll
     * need to determine how these changes affect which messages are valid.
     * <p>
     * Fortunately, many of the rules are unaffected by this change; it might help
     * to start by looking at which rules always match the same set of values and
     * how those rules (especially rules 42 and 31) are used by the new versions
     * of rules 8 and 11.
     * <p>
     * (Remember, you only need to handle the rules you have; building a solution
     * that could handle any hypothetical combination of rules would be
     * significantly more difficult.)
     * <p>
     * For example:
     * <p>
     * 42: 9 14 | 10 1
     * 9: 14 27 | 1 26
     * 10: 23 14 | 28 1
     * 1: "a"
     * 11: 42 31
     * 5: 1 14 | 15 1
     * 19: 14 1 | 14 14
     * 12: 24 14 | 19 1
     * 16: 15 1 | 14 14
     * 31: 14 17 | 1 13
     * 6: 14 14 | 1 14
     * 2: 1 24 | 14 4
     * 0: 8 11
     * 13: 14 3 | 1 12
     * 15: 1 | 14
     * 17: 14 2 | 1 7
     * 23: 25 1 | 22 14
     * 28: 16 1
     * 4: 1 1
     * 20: 14 14 | 1 15
     * 3: 5 14 | 16 1
     * 27: 1 6 | 14 18
     * 14: "b"
     * 21: 14 1 | 1 14
     * 25: 1 1 | 1 14
     * 22: 14 14
     * 8: 42
     * 26: 14 22 | 1 20
     * 18: 15 15
     * 7: 14 5 | 1 21
     * 24: 14 1
     * <p>
     * abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa
     * bbabbbbaabaabba
     * babbbbaabbbbbabbbbbbaabaaabaaa
     * aaabbbbbbaaaabaababaabababbabaaabbababababaaa
     * bbbbbbbaaaabbbbaaabbabaaa
     * bbbababbbbaaaaaaaabbababaaababaabab
     * ababaaaaaabaaab
     * ababaaaaabbbaba
     * baabbaaaabbaaaababbaababb
     * abbbbabbbbaaaababbbbbbaaaababb
     * aaaaabbaabaaaaababaa
     * aaaabbaaaabbaaa
     * aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
     * babaaabbbaaabaababbaabababaaab
     * aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
     * <p>
     * Without updating rules 8 and 11, these rules only match three messages: bbabbbbaabaabba, ababaaaaaabaaab, and ababaaaaabbbaba.
     * <p>
     * However, after updating rules 8 and 11, a total of 12 messages match:
     * <p>
     * bbabbbbaabaabba
     * babbbbaabbbbbabbbbbbaabaaabaaa
     * aaabbbbbbaaaabaababaabababbabaaabbababababaaa
     * bbbbbbbaaaabbbbaaabbabaaa
     * bbbababbbbaaaaaaaabbababaaababaabab
     * ababaaaaaabaaab
     * ababaaaaabbbaba
     * baabbaaaabbaaaababbaababb
     * abbbbabbbbaaaababbbbbbaaaababb
     * aaaaabbaabaaaaababaa
     * aaaabbaabbaaaaaaabbbabbbaaabbaabaaa
     * aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba
     * <p>
     * After updating rules 8 and 11, how many messages completely match rule 0?
     */
    @Test
    void inputMatchRules() throws IOException {
        List<String> input = FileUtils.readLines("/day/19/input");

        assertThat(matchRules(input, false)).isEqualTo(176);
        assertThat(matchRules(input, true)).isEqualTo(352);
    }

    private static record SimpleRule(String pattern) implements Predicate<String> {
        @Override
        public boolean test(String s) {
            return pattern.equals(s);
        }
    }

    private static class CompositeRule implements Predicate<String> {
        private final Map<Integer, Predicate<String>> allRules;
        private final int[] rules;
        private final Map<String, Boolean> cache = new HashMap<>();

        private CompositeRule(Map<Integer, Predicate<String>> allRules, int[] rules) {
            this.allRules = allRules;
            this.rules = rules;
        }

        @Override
        public boolean test(String s) {
            Boolean value = cache.get(s);
            if (value != null) {
                return value;
            }

            value = match(s);
            cache.put(s, value);
            return value;
            // return cache.computeIfAbsent(s, this::match);
        }

        private boolean match(String s) {
            switch (rules.length) {
                case 1:
                    return allRules.get(rules[0]).test(s);
                case 2: {
                    Predicate<String> r1 = allRules.get(rules[0]);
                    Predicate<String> r2 = allRules.get(rules[1]);
                    int length = s.length();
                    for (int i = 1; i < length; ++i) {
                        String s1 = s.substring(0, i);
                        String s2 = s.substring(i);
                        if (r1.test(s1) && r2.test(s2)) {
                            return true;
                        }
                    }
                }
                break;
                case 3: {
                    Predicate<String> r1 = allRules.get(rules[0]);
                    Predicate<String> r2 = allRules.get(rules[1]);
                    Predicate<String> r3 = allRules.get(rules[2]);

                    int length = s.length();
                    for (int i = 1; i < length - 1; ++i) {
                        String s1 = s.substring(0, i);
                        if (r1.test(s1)) {
                            for (int j = i + 1; j < length; ++j) {
                                String s2 = s.substring(i, j);
                                String s3 = s.substring(j);
                                if (r2.test(s2) && r3.test(s3)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                break;
                default:
                    throw new IllegalStateException("Unmanaged rules size: " + Arrays.toString(rules));
            }
            return false;
        }
    }

}
