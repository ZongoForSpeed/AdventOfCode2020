package com.adventofcode.map;

public record Point2D(int x, int y) {
    public static Point2D of(int x, int y) {
        return new Point2D(x, y);
    }
}

