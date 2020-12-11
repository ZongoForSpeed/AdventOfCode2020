package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day11Test {
    private static final List<Pair<Integer, Integer>> ADJACENT_SEATS =
            List.of(Pair.of(-1, -1),
                    Pair.of(-1, 0),
                    Pair.of(-1, 1),
                    Pair.of(0, -1),
                    Pair.of(0, 1),
                    Pair.of(1, -1),
                    Pair.of(1, 0),
                    Pair.of(1, 1)
            );

    private static int adjacentOccupiedSeats(char[][] grid, int x0, int y0, boolean deepSearch) {
        int lengthX = grid.length;
        int lengthY = grid[0].length;

        int result = 0;
        for (Pair<Integer, Integer> delta : ADJACENT_SEATS) {
            int x = x0 + delta.getLeft();
            int y = y0 + delta.getRight();
            if (deepSearch) {
                boolean occupiedSeats = false;
                while (x >= 0 && x < lengthX && y >= 0 && y < lengthY && !occupiedSeats) {
                    if (grid[x][y] == 'L') {
                        break;
                    }
                    occupiedSeats = grid[x][y] == '#';
                    x += delta.getLeft();
                    y += delta.getRight();
                }
                if (occupiedSeats) {
                    result++;
                }
            } else if (x >= 0 && x < lengthX && y >= 0 && y < lengthY && grid[x][y] == '#') {
                result++;
            }
        }

        return result;
    }

    private static char[][] nextStep(char[][] grid, boolean deepSearch) {
        int lengthX = grid.length;
        int lengthY = grid[0].length;

        char[][] nextGrid = new char[lengthX][lengthY];
        for (int x = 0; x < lengthX; ++x) {
            for (int y = 0; y < lengthY; ++y) {
                switch (grid[x][y]) {
                    case '.':
                        nextGrid[x][y] = '.';
                        break;
                    case 'L':
                        if (adjacentOccupiedSeats(grid, x, y, deepSearch) == 0) {
                            nextGrid[x][y] = '#';
                        } else {
                            nextGrid[x][y] = 'L';
                        }
                        break;
                    case '#':
                        if (adjacentOccupiedSeats(grid, x, y, deepSearch) >= 4 + (deepSearch ? 1 : 0)) {
                            nextGrid[x][y] = 'L';
                        } else {
                            nextGrid[x][y] = '#';
                        }
                        break;
                }
            }

        }
        return nextGrid;
    }

    private static long simulationSeatingSystem(char[][] grid, boolean deepSearch) {
        boolean stable;
        do {
            char[][] nextStep = nextStep(grid, deepSearch);
            stable = Arrays.deepEquals(nextStep, grid);
            grid = nextStep;
        } while (!stable);

        long result = 0;
        for (char[] ligne : grid) {
            for (char c : ligne) {
                if (c == '#') {
                    result++;
                }
            }
        }

        return result;
    }

    @Test
    void testSeatingSystem() {
        List<String> inputGrid = List.of(
                "L.LL.LL.LL",
                "LLLLLLL.LL",
                "L.L.L..L..",
                "LLLL.LL.LL",
                "L.LL.LL.LL",
                "L.LLLLL.LL",
                "..L.L.....",
                "LLLLLLLLLL",
                "L.LLLLLL.L",
                "L.LLLLL.LL");

        char[][] grid = inputGrid.stream().map(String::toCharArray).toArray(char[][]::new);

        assertThat(simulationSeatingSystem(grid, false)).isEqualTo(37);
        assertThat(simulationSeatingSystem(grid, true)).isEqualTo(26);
    }

    @Test
    void testAdjacentOccupiedSeats1() {
        List<String> inputGrid = List.of(
                ".......#.",
                "...#.....",
                ".#.......",
                ".........",
                "..#L....#",
                "....#....",
                ".........",
                "#........",
                "...#.....");
        char[][] grid = inputGrid.stream().map(String::toCharArray).toArray(char[][]::new);
        int occupiedSeats = adjacentOccupiedSeats(grid, 4, 3, true);
        assertThat(occupiedSeats).isEqualTo(8);
    }

    @Test
    void testAdjacentOccupiedSeats2() {
        List<String> inputGrid = List.of(
                ".............",
                ".L.L.#.#.#.#.",
                ".............");
        char[][] grid = inputGrid.stream().map(String::toCharArray).toArray(char[][]::new);
        int occupiedSeats = adjacentOccupiedSeats(grid, 1, 1, true);
        assertThat(occupiedSeats).isEqualTo(0);
    }

    @Test
    void testAdjacentOccupiedSeats3() {
        List<String> inputGrid1 = List.of(
                ".##.##.",
                "#.#.#.#",
                "##...##",
                "...L...",
                "##...##",
                "#.#.#.#",
                ".##.##.");

        char[][] grid1 = inputGrid1.stream().map(String::toCharArray).toArray(char[][]::new);
        int occupiedSeats = adjacentOccupiedSeats(grid1, 3, 3, true);
        assertThat(occupiedSeats).isEqualTo(0);
    }

    /**
     * --- Day 11: Seating System ---
     * <p>
     * Your plane lands with plenty of time to spare. The final leg of your
     * journey is a ferry that goes directly to the tropical island where you can
     * finally start your vacation. As you reach the waiting area to board the
     * ferry, you realize you're so early, nobody else has even arrived yet!
     * <p>
     * By modeling the process people use to choose (or abandon) their seat in the
     * waiting area, you're pretty sure you can predict the best place to sit. You
     * make a quick map of the seat layout (your puzzle input).
     * <p>
     * The seat layout fits neatly on a grid. Each position is either floor (.),
     * an empty seat (L), or an occupied seat (#). For example, the initial seat
     * layout might look like this:
     * <p>
     * L.LL.LL.LL
     * LLLLLLL.LL
     * L.L.L..L..
     * LLLL.LL.LL
     * L.LL.LL.LL
     * L.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLLL
     * L.LLLLLL.L
     * L.LLLLL.LL
     * <p>
     * Now, you just need to model the people who will be arriving shortly.
     * Fortunately, people are entirely predictable and always follow a simple set
     * of rules. All decisions are based on the number of occupied seats adjacent
     * to a given seat (one of the eight positions immediately up, down, left,
     * right, or diagonal from the seat). The following rules are applied to every
     * seat simultaneously:
     * <p>
     * - If a seat is empty (L) and there are no occupied seats adjacent to it,
     * the seat becomes occupied.
     * - If a seat is occupied (#) and four or more seats adjacent to it are
     * also occupied, the seat becomes empty.
     * - Otherwise, the seat's state does not change.
     * <p>
     * Floor (.) never changes; seats don't move, and nobody sits on the floor.
     * <p>
     * After one round of these rules, every seat in the example layout becomes occupied:
     * <p>
     * #.##.##.##
     * #######.##
     * #.#.#..#..
     * ####.##.##
     * #.##.##.##
     * #.#####.##
     * ..#.#.....
     * ##########
     * #.######.#
     * #.#####.##
     * <p>
     * After a second round, the seats with four or more occupied adjacent seats become empty again:
     * <p>
     * #.LL.L#.##
     * #LLLLLL.L#
     * L.L.L..L..
     * #LLL.LL.L#
     * #.LL.LL.LL
     * #.LLLL#.##
     * ..L.L.....
     * #LLLLLLLL#
     * #.LLLLLL.L
     * #.#LLLL.##
     * <p>
     * This process continues for three more rounds:
     * <p>
     * #.##.L#.##
     * #L###LL.L#
     * L.#.#..#..
     * #L##.##.L#
     * #.##.LL.LL
     * #.###L#.##
     * ..#.#.....
     * #L######L#
     * #.LL###L.L
     * #.#L###.##
     * <p>
     * #.#L.L#.##
     * #LLL#LL.L#
     * L.L.L..#..
     * #LLL.##.L#
     * #.LL.LL.LL
     * #.LL#L#.##
     * ..L.L.....
     * #L#LLLL#L#
     * #.LLLLLL.L
     * #.#L#L#.##
     * <p>
     * #.#L.L#.##
     * #LLL#LL.L#
     * L.#.L..#..
     * #L##.##.L#
     * #.#L.LL.LL
     * #.#L#L#.##
     * ..L.L.....
     * #L#L##L#L#
     * #.LLLLLL.L
     * #.#L#L#.##
     * <p>
     * At this point, something interesting happens: the chaos stabilizes and
     * further applications of these rules cause no seats to change state! Once
     * people stop moving around, you count 37 occupied seats.
     * <p>
     * Simulate your seating area by applying the seating rules repeatedly until
     * no seats change state. How many seats end up occupied?
     * <p>
     * --- Part Two ---
     * <p>
     * As soon as people start to arrive, you realize your mistake. People don't
     * just care about adjacent seats - they care about the first seat they can
     * see in each of those eight directions!
     * <p>
     * Now, instead of considering just the eight immediately adjacent seats,
     * consider the first seat in each of those eight directions. For example, the
     * empty seat below would see eight occupied seats:
     * <p>
     * .......#.
     * ...#.....
     * .#.......
     * .........
     * ..#L....#
     * ....#....
     * .........
     * #........
     * ...#.....
     * <p>
     * The leftmost empty seat below would only see one empty seat, but cannot see
     * any of the occupied ones:
     * <p>
     * .............
     * .L.L.#.#.#.#.
     * .............
     * <p>
     * The empty seat below would see no occupied seats:
     * <p>
     * .##.##.
     * #.#.#.#
     * ##...##
     * ...L...
     * ##...##
     * #.#.#.#
     * .##.##.
     * <p>
     * Also, people seem to be more tolerant than you expected: it now takes five
     * or more visible occupied seats for an occupied seat to become empty (rather
     * than four or more from the previous rules). The other rules still apply:
     * empty seats that see no occupied seats become occupied, seats matching no rule don't change, and floor never changes.
     * <p>
     * Given the same starting layout as above, these new rules cause the seating
     * area to shift around as follows:
     * <p>
     * L.LL.LL.LL
     * LLLLLLL.LL
     * L.L.L..L..
     * LLLL.LL.LL
     * L.LL.LL.LL
     * L.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLLL
     * L.LLLLLL.L
     * L.LLLLL.LL
     * <p>
     * #.##.##.##
     * #######.##
     * #.#.#..#..
     * ####.##.##
     * #.##.##.##
     * #.#####.##
     * ..#.#.....
     * ##########
     * #.######.#
     * #.#####.##
     * <p>
     * #.LL.LL.L#
     * #LLLLLL.LL
     * L.L.L..L..
     * LLLL.LL.LL
     * L.LL.LL.LL
     * L.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLL#
     * #.LLLLLL.L
     * #.LLLLL.L#
     * <p>
     * #.L#.##.L#
     * #L#####.LL
     * L.#.#..#..
     * ##L#.##.##
     * #.##.#L.##
     * #.#####.#L
     * ..#.#.....
     * LLL####LL#
     * #.L#####.L
     * #.L####.L#
     * <p>
     * #.L#.L#.L#
     * #LLLLLL.LL
     * L.L.L..#..
     * ##LL.LL.L#
     * L.LL.LL.L#
     * #.LLLLL.LL
     * ..L.L.....
     * LLLLLLLLL#
     * #.LLLLL#.L
     * #.L#LL#.L#
     * <p>
     * #.L#.L#.L#
     * #LLLLLL.LL
     * L.L.L..#..
     * ##L#.#L.L#
     * L.L#.#L.L#
     * #.L####.LL
     * ..#.#.....
     * LLL###LLL#
     * #.LLLLL#.L
     * #.L#LL#.L#
     * <p>
     * #.L#.L#.L#
     * #LLLLLL.LL
     * L.L.L..#..
     * ##L#.#L.L#
     * L.L#.LL.L#
     * #.LLLL#.LL
     * ..#.L.....
     * LLL###LLL#
     * #.LLLLL#.L
     * #.L#LL#.L#
     * Again, at this point, people stop shifting around and the seating area reaches equilibrium. Once this occurs, you count 26 occupied seats.
     * <p>
     * Given the new visibility method and the rule change for occupied seats becoming empty, once equilibrium is reached, how many seats end up occupied?
     */
    @Test
    void inputSeatingSystem() throws IOException {
        List<String> inputGrid = FileUtils.readLines("/day/11/input");

        char[][] grid = inputGrid.stream().map(String::toCharArray).toArray(char[][]::new);

        assertThat(simulationSeatingSystem(grid, false)).isEqualTo(2472);
        assertThat(simulationSeatingSystem(grid, true)).isEqualTo(2197);
    }
}
