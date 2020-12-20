package com.adventofcode.map;

public record Point4D(int x, int y, int z, int w) {
    public static Point4D of(int x, int y, int z, int w) {
        return new Point4D(x, y, z, w);
    }
}
