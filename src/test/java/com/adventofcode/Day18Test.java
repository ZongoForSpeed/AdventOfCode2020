package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.BinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

public class Day18Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day18Test.class);

    private static long evalExpressionLR(Iterator<Character> iterator) {
        Stack<Long> stack = new Stack<>();
        BinaryOperator<Long> operation = null;
        mainLoop:
        while (iterator.hasNext()) {
            char currentToken = iterator.next();
            switch (currentToken) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    stack.add((long) (currentToken - '0'));
                    break;
                case '(':
                    stack.add(evalExpressionLR(iterator));
                    break;
                case ')':
                    break mainLoop;
                case '+':
                    if (operation != null) {
                        stack.add(operation.apply(stack.pop(), stack.pop()));
                    }
                    operation = Long::sum;
                    break;
                case '*':
                    if (operation != null) {
                        stack.add(operation.apply(stack.pop(), stack.pop()));
                    }
                    operation = (a, b) -> a * b;
                    break;
            }
        }

        if (operation != null) {
            stack.add(operation.apply(stack.pop(), stack.pop()));
        }

        return stack.pop();
    }

    private static long evalExpressionLR(String expression) {
        List<Character> characterList = new ArrayList<>();
        for (char c : expression.toCharArray()) {
            characterList.add(c);
        }

        long eval = evalExpressionLR(characterList.iterator());
        LOGGER.info("{} becomes {}.", expression, eval);
        return eval;
    }

    private static long evalExpressionAdditionFirst(Iterator<Character> iterator) {
        Stack<Long> stack = new Stack<>();
        BinaryOperator<Long> operation = null;
        mainLoop:
        while (iterator.hasNext()) {
            char currentToken = iterator.next();
            switch (currentToken) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    stack.add((long) (currentToken - '0'));
                    break;
                case '(':
                    stack.add(evalExpressionAdditionFirst(iterator));
                    break;
                case ')':
                    break mainLoop;
                case '+':
                    if (operation != null) {
                        stack.add(operation.apply(stack.pop(), stack.pop()));
                    }
                    operation = Long::sum;
                    break;
                case '*':
                    if (operation != null) {
                        stack.add(operation.apply(stack.pop(), stack.pop()));
                    }
                    operation = null;
                    break;
            }
        }

        if (operation != null) {
            stack.add(operation.apply(stack.pop(), stack.pop()));
        }

        return stack.stream().mapToLong(t -> t).reduce(1, (a, b) -> a * b);
    }

    private static long evalExpressionAdditionFirst(String expression) {
        List<Character> characterList = new ArrayList<>();
        for (char c : expression.toCharArray()) {
            characterList.add(c);
        }

        long eval = evalExpressionAdditionFirst(characterList.iterator());
        LOGGER.info("{} becomes {}.", expression, eval);
        return eval;
    }

    @Test
    void testReadExpression() {
        assertThat(evalExpressionLR("1 + 2 * 3 + 4 * 5 + 6")).isEqualTo(71);
        assertThat(evalExpressionLR("1 + (2 * 3) + (4 * (5 + 6))")).isEqualTo(51);
        assertThat(evalExpressionLR("2 * 3 + (4 * 5)")).isEqualTo(26);
        assertThat(evalExpressionLR("5 + (8 * 3 + 9 + 3 * 4 * 3)")).isEqualTo(437);
        assertThat(evalExpressionLR("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")).isEqualTo(12240);
        assertThat(evalExpressionLR("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")).isEqualTo(13632);

        assertThat(evalExpressionAdditionFirst("1 + 2 * 3 + 4 * 5 + 6")).isEqualTo(231);
        assertThat(evalExpressionAdditionFirst("1 + (2 * 3) + (4 * (5 + 6))")).isEqualTo(51);
        assertThat(evalExpressionAdditionFirst("2 * 3 + (4 * 5)")).isEqualTo(46);
        assertThat(evalExpressionAdditionFirst("5 + (8 * 3 + 9 + 3 * 4 * 3)")).isEqualTo(1445);
        assertThat(evalExpressionAdditionFirst("5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))")).isEqualTo(669060);
        assertThat(evalExpressionAdditionFirst("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2")).isEqualTo(23340);
    }

    /**
     * --- Day 18: Operation Order ---
     * <p>
     * As you look out the window and notice a heavily-forested continent slowly
     * appear over the horizon, you are interrupted by the child sitting next to
     * you. They're curious if you could help them with their math homework.
     * <p>
     * Unfortunately, it seems like this "math" follows different rules than you
     * remember.
     * <p>
     * The homework (your puzzle input) consists of a series of expressions that
     * consist of addition (+), multiplication (*), and parentheses ((...)). Just
     * like normal math, parentheses indicate that the expression inside must be
     * evaluated before it can be used by the surrounding expression. Addition
     * still finds the sum of the numbers on both sides of the operator, and
     * multiplication still finds the product.
     * <p>
     * However, the rules of operator precedence have changed. Rather than
     * evaluating multiplication before addition, the operators have the same
     * precedence, and are evaluated left-to-right regardless of the order in
     * which they appear.
     * <p>
     * For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are
     * as follows:
     * <p>
     * 1 + 2 * 3 + 4 * 5 + 6
     * 3   * 3 + 4 * 5 + 6
     * 9   + 4 * 5 + 6
     * 13   * 5 + 6
     * 65   + 6
     * 71
     * <p>
     * Parentheses can override this order; for example, here is what happens if
     * parentheses are added to form 1 + (2 * 3) + (4 * (5 + 6)):
     * <p>
     * 1 + (2 * 3) + (4 * (5 + 6))
     * 1 +    6    + (4 * (5 + 6))
     * 7      + (4 * (5 + 6))
     * 7      + (4 *   11   )
     * 7      +     44
     * 51
     * <p>
     * Here are a few more examples:
     * <p>
     * 2 * 3 + (4 * 5) becomes 26.
     * 5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 437.
     * 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 12240.
     * ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 13632.
     * <p>
     * Before you can help with the homework, you need to understand it yourself.
     * Evaluate the expression on each line of the homework; what is the sum of
     * the resulting values?
     * <p>
     * --- Part Two ---
     * <p>
     * You manage to answer the child's questions and they finish part 1 of their
     * homework, but get stuck when they reach the next section: advanced math.
     * <p>
     * Now, addition and multiplication have different precedence levels, but
     * they're not the ones you're familiar with. Instead, addition is evaluated
     * before multiplication.
     * <p>
     * For example, the steps to evaluate the expression 1 + 2 * 3 + 4 * 5 + 6 are
     * now as follows:
     * <p>
     * 1 + 2 * 3 + 4 * 5 + 6
     * 3   * 3 + 4 * 5 + 6
     * 3   *   7   * 5 + 6
     * 3   *   7   *  11
     * 21       *  11
     * 231
     * Here are the other examples from above:
     * <p>
     * 1 + (2 * 3) + (4 * (5 + 6)) still becomes 51.
     * 2 * 3 + (4 * 5) becomes 46.
     * 5 + (8 * 3 + 9 + 3 * 4 * 3) becomes 1445.
     * 5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4)) becomes 669060.
     * ((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2 becomes 23340.
     * <p>
     * What do you get if you add up the results of evaluating the homework
     * problems using these new rules?
     */
    @Test
    void inputReadExpression() throws IOException {
        List<String> lines = FileUtils.readLines("/day/18/input");
        assertThat(lines.stream().mapToLong(Day18Test::evalExpressionLR).sum()).isEqualTo(654686398176L);
        assertThat(lines.stream().mapToLong(Day18Test::evalExpressionAdditionFirst).sum()).isEqualTo(8952864356993L);
    }
}
