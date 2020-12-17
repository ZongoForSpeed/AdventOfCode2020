package com.adventofcode;

import com.adventofcode.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Day17Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day17Test.class);

    private static Collection<Position> neighbors(Position p) {
        int x = p.x();
        int y = p.y();
        int z = p.z();

        List<Position> result = new ArrayList<>();

        for (int dx = -1; dx <= 1; ++dx)
            for (int dy = -1; dy <= 1; ++dy)
                for (int dz = -1; dz <= 1; ++dz) {
                    if (dx != 0 || dy != 0 || dz != 0) {
                        result.add(Position.of(x + dx, y + dy, z + dz));
                    }
                }
        return result;
    }

    private static Collection<HyperPosition> neighbors(HyperPosition p) {
        int x = p.x();
        int y = p.y();
        int z = p.z();
        int w = p.w();

        List<HyperPosition> result = new ArrayList<>();

        for (int dx = -1; dx <= 1; ++dx)
            for (int dy = -1; dy <= 1; ++dy)
                for (int dz = -1; dz <= 1; ++dz)
                    for (int dw = -1; dw <= 1; ++dw) {
                        if (dx != 0 || dy != 0 || dz != 0 || dw != 0) {
                            result.add(HyperPosition.of(x + dx, y + dy, z + dz, w + dw));
                        }
                    }
        return result;
    }

    private static long runConwayCubes(List<String> initialState, int iteration) {
        Set<Position> state = new HashSet<>();

        for (int y = 0, initialStateSize = initialState.size(); y < initialStateSize; y++) {
            char[] line = initialState.get(y).toCharArray();
            for (int x = 0, lineLength = line.length; x < lineLength; x++) {
                char c = line[x];
                if (c == '#') {
                    state.add(Position.of(x, y, 0));
                }
            }
        }

        LOGGER.info("initialState: {}", state);
        for (int i = 1; i <= iteration; ++i) {
            Set<Position> nextState = new HashSet<>();
            int minX = state.stream().mapToInt(Position::x).min().orElseThrow();
            int minY = state.stream().mapToInt(Position::y).min().orElseThrow();
            int minZ = state.stream().mapToInt(Position::z).min().orElseThrow();
            int maxX = state.stream().mapToInt(Position::x).max().orElseThrow();
            int maxY = state.stream().mapToInt(Position::y).max().orElseThrow();
            int maxZ = state.stream().mapToInt(Position::z).max().orElseThrow();
            for (int x = minX - 1; x <= maxX + 1; ++x) {
                for (int y = minY - 1; y <= maxY + 1; ++y) {
                    for (int z = minZ - 1; z <= maxZ + 1; ++z) {
                        Position position = Position.of(x, y, z);
                        long count = neighbors(position).stream().filter(state::contains).count();
                        if (state.contains(position)) {
                            if (count == 2 || count == 3) {
                                nextState.add(position);
                            }
                        } else if (count == 3) {
                            nextState.add(position);
                        }
                    }
                }
            }

            state = nextState;
            // LOGGER.info("State at {}: {}", i, state);
        }
        return state.size();
    }

    private static long runConwayHyperCubes(List<String> initialState, int iteration) {
        Set<HyperPosition> state = new HashSet<>();

        for (int y = 0, initialStateSize = initialState.size(); y < initialStateSize; y++) {
            char[] line = initialState.get(y).toCharArray();
            for (int x = 0, lineLength = line.length; x < lineLength; x++) {
                char c = line[x];
                if (c == '#') {
                    state.add(HyperPosition.of(x, y, 0, 0));
                }
            }
        }

        LOGGER.info("initialState: {}", state);
        for (int i = 1; i <= iteration; ++i) {
            Set<HyperPosition> nextState = new HashSet<>();
            int minX = state.stream().mapToInt(HyperPosition::x).min().orElseThrow();
            int minY = state.stream().mapToInt(HyperPosition::y).min().orElseThrow();
            int minZ = state.stream().mapToInt(HyperPosition::z).min().orElseThrow();
            int minW = state.stream().mapToInt(HyperPosition::w).min().orElseThrow();
            int maxX = state.stream().mapToInt(HyperPosition::x).max().orElseThrow();
            int maxY = state.stream().mapToInt(HyperPosition::y).max().orElseThrow();
            int maxZ = state.stream().mapToInt(HyperPosition::z).max().orElseThrow();
            int maxW = state.stream().mapToInt(HyperPosition::w).max().orElseThrow();
            for (int x = minX - 1; x <= maxX + 1; ++x) {
                for (int y = minY - 1; y <= maxY + 1; ++y) {
                    for (int z = minZ - 1; z <= maxZ + 1; ++z) {
                        for (int w = minW - 1; w <= maxW + 1; ++w) {
                            HyperPosition position = HyperPosition.of(x, y, z, w);
                            long count = neighbors(position).stream().filter(state::contains).count();
                            if (state.contains(position)) {
                                if (count == 2 || count == 3) {
                                    nextState.add(position);
                                }
                            } else if (count == 3) {
                                nextState.add(position);
                            }
                        }
                    }
                }
            }

            state = nextState;
            // LOGGER.info("State at {}: {}", i, state);
        }
        return state.size();
    }

    @Test
    void testConwayCubes() {
        List<String> initialState = List.of(".#.",
                "..#",
                "###");

        assertThat(runConwayCubes(initialState, 6)).isEqualTo(112);
        assertThat(runConwayHyperCubes(initialState, 6)).isEqualTo(848);
    }

    /**
     * --- Day 17: Conway Cubes ---
     * As your flight slowly drifts through the sky, the Elves at the Mythical
     * Information Bureau at the North Pole contact you. They'd like some help
     * debugging a malfunctioning experimental energy source aboard one of their
     * super-secret imaging satellites.
     * <p>
     * The experimental energy source is based on cutting-edge technology: a set
     * of Conway Cubes contained in a pocket dimension! When you hear it's having
     * problems, you can't help but agree to take a look.
     * <p>
     * The pocket dimension contains an infinite 3-dimensional grid. At every
     * integer 3-dimensional coordinate (x,y,z), there exists a single cube which
     * is either active or inactive.
     * <p>
     * In the initial state of the pocket dimension, almost all cubes start
     * inactive. The only exception to this is a small flat region of cubes (your
     * puzzle input); the cubes in this region start in the specified active (#)
     * or inactive (.) state.
     * <p>
     * The energy source then proceeds to boot up by executing six cycles.
     * <p>
     * Each cube only ever considers its neighbors: any of the 26 other cubes
     * where any of their coordinates differ by at most 1. For example, given the
     * cube at x=1,y=2,z=3, its neighbors include the cube at x=2,y=2,z=2, the
     * cube at x=0,y=2,z=3, and so on.
     * <p>
     * During a cycle, all cubes simultaneously change their state according to
     * the following rules:
     * <p>
     * - If a cube is active and exactly 2 or 3 of its neighbors are also
     * active, the cube remains active. Otherwise, the cube becomes inactive.
     * - If a cube is inactive but exactly 3 of its neighbors are active, the
     * cube becomes active. Otherwise, the cube remains inactive.
     * <p>
     * The engineers responsible for this experimental energy source would like
     * you to simulate the pocket dimension and determine what the configuration
     * of cubes should be at the end of the six-cycle boot process.
     * <p>
     * For example, consider the following initial state:
     * <p>
     * .#.
     * ..#
     * ###
     * <p>
     * Even though the pocket dimension is 3-dimensional, this initial state
     * represents a small 2-dimensional slice of it. (In particular, this initial
     * state defines a 3x3x1 region of the 3-dimensional space.)
     * <p>
     * Simulating a few cycles from this initial state produces the following
     * configurations, where the result of each cycle is shown layer-by-layer at
     * each given z coordinate (and the frame of view follows the active cells in
     * each cycle):
     * <p>
     * Before any cycles:
     * <p>
     * z=0
     * .#.
     * ..#
     * ###
     * <p>
     * After 1 cycle:
     * <p>
     * z=-1
     * #..
     * ..#
     * .#.
     * <p>
     * z=0
     * #.#
     * .##
     * .#.
     * <p>
     * z=1
     * #..
     * ..#
     * .#.
     * <p>
     * <p>
     * After 2 cycles:
     * <p>
     * z=-2
     * .....
     * .....
     * ..#..
     * .....
     * .....
     * <p>
     * z=-1
     * ..#..
     * .#..#
     * ....#
     * .#...
     * .....
     * <p>
     * z=0
     * ##...
     * ##...
     * #....
     * ....#
     * .###.
     * <p>
     * z=1
     * ..#..
     * .#..#
     * ....#
     * .#...
     * .....
     * <p>
     * z=2
     * .....
     * .....
     * ..#..
     * .....
     * .....
     * <p>
     * <p>
     * After 3 cycles:
     * <p>
     * z=-2
     * .......
     * .......
     * ..##...
     * ..###..
     * .......
     * .......
     * .......
     * <p>
     * z=-1
     * ..#....
     * ...#...
     * #......
     * .....##
     * .#...#.
     * ..#.#..
     * ...#...
     * <p>
     * z=0
     * ...#...
     * .......
     * #......
     * .......
     * .....##
     * .##.#..
     * ...#...
     * <p>
     * z=1
     * ..#....
     * ...#...
     * #......
     * .....##
     * .#...#.
     * ..#.#..
     * ...#...
     * <p>
     * z=2
     * .......
     * .......
     * ..##...
     * ..###..
     * .......
     * .......
     * .......
     * <p>
     * After the full six-cycle boot process completes, 112 cubes are left in the
     * active state.
     * <p>
     * Starting with your given initial configuration, simulate six cycles. How
     * many cubes are left in the active state after the sixth cycle?
     * <p>
     * --- Part Two ---
     * For some reason, your simulated results don't match what the experimental
     * energy source engineers expected. Apparently, the pocket dimension actually
     * has four spatial dimensions, not three.
     * <p>
     * The pocket dimension contains an infinite 4-dimensional grid. At every
     * integer 4-dimensional coordinate (x,y,z,w), there exists a single cube
     * (really, a hypercube) which is still either active or inactive.
     * <p>
     * Each cube only ever considers its neighbors: any of the 80 other cubes
     * where any of their coordinates differ by at most 1. For example, given the
     * cube at x=1,y=2,z=3,w=4, its neighbors include the cube at x=2,y=2,z=3,w=3,
     * the cube at x=0,y=2,z=3,w=4, and so on.
     * <p>
     * The initial state of the pocket dimension still consists of a small flat
     * region of cubes. Furthermore, the same rules for cycle updating still
     * apply: during each cycle, consider the number of active neighbors of each
     * cube.
     * <p>
     * For example, consider the same initial state as in the example above. Even
     * though the pocket dimension is 4-dimensional, this initial state represents
     * a small 2-dimensional slice of it. (In particular, this initial state
     * defines a 3x3x1x1 region of the 4-dimensional space.)
     * <p>
     * Simulating a few cycles from this initial state produces the following
     * configurations, where the result of each cycle is shown layer-by-layer at
     * each given z and w coordinate:
     * <p>
     * Before any cycles:
     * <p>
     * z=0, w=0
     * .#.
     * ..#
     * ###
     * <p>
     * <p>
     * After 1 cycle:
     * <p>
     * z=-1, w=-1
     * #..
     * ..#
     * .#.
     * <p>
     * z=0, w=-1
     * #..
     * ..#
     * .#.
     * <p>
     * z=1, w=-1
     * #..
     * ..#
     * .#.
     * <p>
     * z=-1, w=0
     * #..
     * ..#
     * .#.
     * <p>
     * z=0, w=0
     * #.#
     * .##
     * .#.
     * <p>
     * z=1, w=0
     * #..
     * ..#
     * .#.
     * <p>
     * z=-1, w=1
     * #..
     * ..#
     * .#.
     * <p>
     * z=0, w=1
     * #..
     * ..#
     * .#.
     * <p>
     * z=1, w=1
     * #..
     * ..#
     * .#.
     * <p>
     * <p>
     * After 2 cycles:
     * <p>
     * z=-2, w=-2
     * .....
     * .....
     * ..#..
     * .....
     * .....
     * <p>
     * z=-1, w=-2
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=0, w=-2
     * ###..
     * ##.##
     * #...#
     * .#..#
     * .###.
     * <p>
     * z=1, w=-2
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=2, w=-2
     * .....
     * .....
     * ..#..
     * .....
     * .....
     * <p>
     * z=-2, w=-1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=-1, w=-1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=0, w=-1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=1, w=-1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=2, w=-1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=-2, w=0
     * ###..
     * ##.##
     * #...#
     * .#..#
     * .###.
     * <p>
     * z=-1, w=0
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=0, w=0
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=1, w=0
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=2, w=0
     * ###..
     * ##.##
     * #...#
     * .#..#
     * .###.
     * <p>
     * z=-2, w=1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=-1, w=1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=0, w=1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=1, w=1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=2, w=1
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=-2, w=2
     * .....
     * .....
     * ..#..
     * .....
     * .....
     * <p>
     * z=-1, w=2
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=0, w=2
     * ###..
     * ##.##
     * #...#
     * .#..#
     * .###.
     * <p>
     * z=1, w=2
     * .....
     * .....
     * .....
     * .....
     * .....
     * <p>
     * z=2, w=2
     * .....
     * .....
     * ..#..
     * .....
     * .....
     * <p>
     * After the full six-cycle boot process completes, 848 cubes are left in the
     * active state.
     * <p>
     * Starting with your given initial configuration, simulate six cycles in a
     * 4-dimensional space. How many cubes are left in the active state after the
     * sixth cycle?
     */
    @Test
    void inputConwayCubes() throws IOException {
        List<String> initialState = FileUtils.readLines("/day/17/input");

        assertThat(runConwayCubes(initialState, 6)).isEqualTo(215);
        assertThat(runConwayHyperCubes(initialState, 6)).isEqualTo(1728);

    }

    private static record Position(int x, int y, int z) {
        public static Position of(int x, int y, int z) {
            return new Position(x, y, z);
        }
    }

    private static record HyperPosition(int x, int y, int z, int w) {
        public static HyperPosition of(int x, int y, int z, int w) {
            return new HyperPosition(x, y, z, w);
        }
    }
}
