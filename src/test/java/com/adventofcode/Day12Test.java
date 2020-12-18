package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class Day12Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day12Test.class);

    @Test
    void testRainRisk1() {
        List<String> actions = List.of("F10",
                "N3",
                "F7",
                "R90",
                "F11");

        Part1.Position position = Part1.move(actions);
        LOGGER.info("Last position {}", position);
        int manhattanDistance = Math.abs(position.x()) + Math.abs(position.y());
        assertThat(manhattanDistance).isEqualTo(25);
    }

    @Test
    void testRainRisk2() {
        List<String> actions = List.of("F10",
                "N3",
                "F7",
                "R90",
                "F11");

        Part2.Position position = Part2.move(actions);
        LOGGER.info("Last position {}", position);
        int manhattanDistance = Math.abs(position.x()) + Math.abs(position.y());
        assertThat(manhattanDistance).isEqualTo(286);
    }

    /**
     * --- Day 12: Rain Risk ---
     * <p>
     * Your ferry made decent progress toward the island, but the storm came in
     * faster than anyone expected. The ferry needs to take evasive actions!
     * <p>
     * Unfortunately, the ship's navigation computer seems to be malfunctioning;
     * rather than giving a route directly to safety, it produced extremely
     * circuitous instructions. When the captain uses the PA system to ask if
     * anyone can help, you quickly volunteer.
     * <p>
     * The navigation instructions (your puzzle input) consists of a sequence of
     * single-character actions paired with integer input values. After staring at
     * them for a few minutes, you work out what they probably mean:
     * <p>
     * - Action N means to move north by the given value.
     * - Action S means to move south by the given value.
     * - Action E means to move east by the given value.
     * - Action W means to move west by the given value.
     * - Action L means to turn left the given number of degrees.
     * - Action R means to turn right the given number of degrees.
     * - Action F means to move forward by the given value in the direction the
     * ship is currently facing.
     * <p>
     * The ship starts by facing east. Only the L and R actions change the
     * direction the ship is facing. (That is, if the ship is facing east and the
     * next instruction is N10, the ship would move north 10 units, but would
     * still move east if the following action were F.)
     * <p>
     * For example:
     * <p>
     * F10
     * N3
     * F7
     * R90
     * F11
     * <p>
     * These instructions would be handled as follows:
     * <p>
     * - F10 would move the ship 10 units east (because the ship starts by
     * facing east) to east 10, north 0.
     * - N3 would move the ship 3 units north to east 10, north 3.
     * - F7 would move the ship another 7 units east (because the ship is still
     * facing east) to east 17, north 3.
     * - R90 would cause the ship to turn right by 90 degrees and face south;
     * it remains at east 17, north 3.
     * - F11 would move the ship 11 units south to east 17, south 8.
     * <p>
     * At the end of these instructions, the ship's Manhattan distance (sum of the
     * absolute values of its east/west position and its north/south position)
     * from its starting position is 17 + 8 = 25.
     * <p>
     * Figure out where the navigation instructions lead. What is the Manhattan
     * distance between that location and the ship's starting position?
     * <p>
     * --- Part Two ---
     * <p>
     * Before you can give the destination to the captain, you realize that the
     * actual action meanings were printed on the back of the instructions the
     * whole time.
     * <p>
     * Almost all of the actions indicate how to move a waypoint which is relative
     * to the ship's position:
     * <p>
     * - Action N means to move the waypoint north by the given value.
     * - Action S means to move the waypoint south by the given value.
     * - Action E means to move the waypoint east by the given value.
     * - Action W means to move the waypoint west by the given value.
     * - Action L means to rotate the waypoint around the ship left
     * (counter-clockwise) the given number of degrees.
     * - Action R means to rotate the waypoint around the ship right
     * (clockwise) the given number of degrees.
     * - Action F means to move forward to the waypoint a number of times equal
     * to the given value.
     * <p>
     * The waypoint starts 10 units east and 1 unit north relative to the ship.
     * The waypoint is relative to the ship; that is, if the ship moves, the
     * waypoint moves with it.
     * <p>
     * For example, using the same instructions as above:
     * <p>
     * - F10 moves the ship to the waypoint 10 times (a total of 100 units east
     * and 10 units north), leaving the ship at east 100, north 10. The
     * waypoint stays 10 units east and 1 unit north of the ship.
     * - N3 moves the waypoint 3 units north to 10 units east and 4 units north
     * of the ship. The ship remains at east 100, north 10.
     * - F7 moves the ship to the waypoint 7 times (a total of 70 units east
     * and 28 units north), leaving the ship at east 170, north 38. The
     * waypoint stays 10 units east and 4 units north of the ship.
     * - R90 rotates the waypoint around the ship clockwise 90 degrees, moving
     * it to 4 units east and 10 units south of the ship. The ship remains at
     * east 170, north 38.
     * - F11 moves the ship to the waypoint 11 times (a total of 44 units east
     * and 110 units south), leaving the ship at east 214, south 72. The
     * waypoint stays 4 units east and 10 units south of the ship.
     * <p>
     * After these operations, the ship's Manhattan distance from its starting position is 214 + 72 = 286.
     * <p>
     * Figure out where the navigation instructions actually lead. What is the Manhattan distance between that location and the ship's starting position?
     */
    @Test
    void inputRainRisk1() throws IOException {
        List<String> actions = FileUtils.readLines("/day/12/input");

        Part1.Position position = Part1.move(actions);
        LOGGER.info("Last position {}", position);
        int manhattanDistance = Math.abs(position.x()) + Math.abs(position.y());
        assertThat(manhattanDistance).isEqualTo(590);
    }

    @Test
    void inputRainRisk2() throws IOException {
        List<String> actions = FileUtils.readLines("/day/12/input");

        Part2.Position position = Part2.move(actions);
        LOGGER.info("Last position {}", position);
        int manhattanDistance = Math.abs(position.x()) + Math.abs(position.y());
        assertThat(manhattanDistance).isEqualTo(42013);
    }

    private static final class Part1 {
        private static Position move(Position position, String action) {
            Direction direction = position.d();
            int x = position.x();
            int y = position.y();

            char type = action.charAt(0);
            int value = Integer.parseInt(action.substring(1));
            switch (type) {
                case 'N' -> y += value;
                case 'S' -> y -= value;
                case 'E' -> x += value;
                case 'W' -> x -= value;
                case 'L' -> direction = direction.turnLeft(value);
                case 'R' -> direction = direction.turnRight(value);
                case 'F' -> {
                    Pair<Integer, Integer> move = direction.move(x, y, value);
                    x = move.getLeft();
                    y = move.getRight();
                }
                default -> throw new IllegalStateException("Unknown action : " + type);
            }

            return new Position(direction, x, y);
        }

        private static Position move(List<String> actions) {
            Position position = new Position(Direction.EAST, 0, 0);
            for (String action : actions) {
                position = move(position, action);
                LOGGER.debug("Position after move {}: {}", action, position);
            }

            return position;
        }

        enum Direction {
            NORTH {
                @Override
                public Pair<Integer, Integer> move(int x, int y, int n) {
                    return Pair.of(x, y + n);
                }
            },
            WEST {
                @Override
                public Pair<Integer, Integer> move(int x, int y, int n) {
                    return Pair.of(x - n, y);
                }
            },
            SOUTH {
                @Override
                public Pair<Integer, Integer> move(int x, int y, int n) {
                    return Pair.of(x, y - n);
                }
            },
            EAST {
                @Override
                public Pair<Integer, Integer> move(int x, int y, int n) {
                    return Pair.of(x + n, y);
                }
            };

            public Direction turnLeft(int n) {
                return values()[(ordinal() + n / 90) % 4];
            }

            public Direction turnRight(int n) {
                return values()[(ordinal() + 4 - n / 90) % 4];

            }

            public abstract Pair<Integer, Integer> move(int x, int y, int n);
        }

        private static record Position(Direction d, int x, int y) {
            @Override
            public String toString() {
                return "Position{" +
                        "d=" + d +
                        ", x=" + x +
                        ", y=" + y +
                        '}';
            }
        }
    }

    private static final class Part2 {
        private static Position move(Position position, String action) {
            int x = position.x();
            int y = position.y();

            int waypointX = position.waypointX();
            int waypointY = position.waypointY();

            char type = action.charAt(0);
            int value = Integer.parseInt(action.substring(1));
            switch (type) {
                case 'N' -> waypointY += value;
                case 'S' -> waypointY -= value;
                case 'E' -> waypointX += value;
                case 'W' -> waypointX -= value;
                case 'L' -> {
                    Pair<Integer, Integer> waypoint = turnLeft(waypointX, waypointY, value);
                    waypointX = waypoint.getLeft();
                    waypointY = waypoint.getRight();
                }
                case 'R' -> {
                    Pair<Integer, Integer> waypoint = turnRight(waypointX, waypointY, value);
                    waypointX = waypoint.getLeft();
                    waypointY = waypoint.getRight();
                }
                case 'F' -> {
                    x += waypointX * value;
                    y += waypointY * value;
                }
                default -> throw new IllegalStateException("Unknown action : " + type);
            }

            return new Position(x, y, waypointX, waypointY);
        }

        private static Position move(List<String> actions) {
            Position position = new Position(0, 0, 10, 1);
            for (String action : actions) {
                position = move(position, action);
                LOGGER.debug("Position after move {}: {}", action, position);
            }

            return position;
        }

        private static Pair<Integer, Integer> turnLeft(int x, int y, int angle) {
            return switch (angle) {
                case 90 -> Pair.of(-y, x);
                case 180 -> Pair.of(-x, -y);
                case 270 -> Pair.of(y, -x);
                default -> throw new IllegalStateException("Cannot turn left : " + angle);
            };
        }

        private static Pair<Integer, Integer> turnRight(int x, int y, int angle) {
            return switch (angle) {
                case 90 -> Pair.of(y, -x);
                case 180 -> Pair.of(-x, -y);
                case 270 -> Pair.of(-y, x);
                default -> throw new IllegalStateException("Cannot turn right : " + angle);
            };
        }

        private static record Position(int x, int y, int waypointX, int waypointY) {
        }
    }
}
