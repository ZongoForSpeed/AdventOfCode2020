package com.adventofcode.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerMap {
    private final int defaultValue;
    private int[][] map;

    public IntegerMap() {
        this(0, 0, 0);
    }

    public IntegerMap(int sizeX, int sizeY, int defaultValue) {
        this.map = new int[sizeY][sizeX];
        this.defaultValue = defaultValue;
        for (int[] line : map) {
            Arrays.fill(line, 0, sizeX, defaultValue);
        }
    }

    public int get(int x, int y) {
        if (y < map.length && x < map[y].length) {
            return map[y][x];
        }
        return defaultValue;
    }

    public int get(Point2D p) {
        return get(p.x(), p.y());
    }

    public void set(int x, int y, int value) {
        if (y >= map.length) {
            int length = map.length;
            map = Arrays.copyOf(map, y + 1);
            Arrays.fill(map, length, y + 1, new int[0]);
        }
        if (x >= map[y].length) {
            int length = map[y].length;
            map[y] = Arrays.copyOf(map[y], x + 1);
            Arrays.fill(map[y], length, x + 1, defaultValue);
        }

        map[y][x] = value;
    }

    @Override
    public String toString() {
        return Arrays.stream(map)
                .map(Arrays::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public List<Point2D> points() {
        List<Point2D> points = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            int[] line = map[y];
            for (int x = 0; x < line.length; x++) {
                if (line[x] != defaultValue) {
                    points.add(Point2D.of(x, y));
                }
            }
        }
        return points;
    }
}
