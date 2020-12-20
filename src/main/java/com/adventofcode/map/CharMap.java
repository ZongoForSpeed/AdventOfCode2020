package com.adventofcode.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CharMap {
    private final char defaultValue;
    private char[][] map;

    public CharMap() {
        this(0, 0, ' ');
    }

    public CharMap(int sizeX, int sizeY, char defaultValue) {
        this.map = new char[sizeY][sizeX];
        this.defaultValue = defaultValue;
        for (char[] line : map) {
            Arrays.fill(line, 0, sizeX, defaultValue);
        }
    }

    public char get(int x, int y) {
        if (y < map.length && x < map[y].length) {
            return map[y][x];
        }
        return defaultValue;
    }

    public char get(Point2D p) {
        return get(p.x(), p.y());
    }

    public void set(int x, int y, char value) {
        if (y >= map.length) {
            int length = map.length;
            map = Arrays.copyOf(map, y + 1);
            Arrays.fill(map, length, y + 1, new char[0]);
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
        return Arrays.stream(map).map(String::valueOf).map(s -> s + System.lineSeparator()).collect(Collectors.joining());
    }

    public List<Point2D> points() {
        List<Point2D> points = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            char[] line = map[y];
            for (int x = 0; x < line.length; x++) {
                if (line[x] != defaultValue) {
                    points.add(Point2D.of(x, y));
                }
            }
        }
        return points;
    }
}
