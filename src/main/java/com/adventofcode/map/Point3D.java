package com.adventofcode.map;

public record Point3D(int x, int y, int z) {
    public static Point3D of(int x, int y, int z) {
        return new Point3D(x, y, z);
    }
}
