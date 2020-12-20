package com.adventofcode;

import com.adventofcode.map.CharMap;
import com.adventofcode.map.IntegerMap;
import com.adventofcode.map.Point2D;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class Day20Test {
    private static final Logger LOGGER = LoggerFactory.getLogger(Day20Test.class);

    private static CharMap readSeaMonster() {
        List<String> inputSeaMonster = List.of(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
        );
        CharMap seaMonster = new CharMap();
        for (int i = 0; i < inputSeaMonster.size(); i++) {
            char[] line = inputSeaMonster.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                if (line[j] == '#') {
                    seaMonster.set(j, i, line[j]);
                }
            }
        }

        return seaMonster;
    }

    public static boolean assembleJurassicJigsaw(Map<Integer, List<CharMap>> tiles, IntegerMap position, int gridX, int gridY, Set<Integer> used, int gridSize, int size, CharMap fullGrid) {
        if (gridY == gridSize) return true;

        int nextGridY = gridY;
        int nextGridX = gridX + 1;
        if (nextGridX == gridSize) {
            nextGridX = 0;
            nextGridY++;
        }

        for (Map.Entry<Integer, List<CharMap>> entry : tiles.entrySet()) {
            int id = entry.getKey();
            if (!used.contains(id)) {
                used.add(id);
                for (CharMap tile : entry.getValue()) {
                    if (check(fullGrid, tile, gridX, gridY, size)) {
                        position.set(gridX, gridY, id);
                        if (assembleJurassicJigsaw(tiles, position, nextGridX, nextGridY, used, gridSize, size, fullGrid))
                            return true;
                    }

                }
                used.remove(id);
            }
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                fullGrid.set(gridX * size + x, gridY * size + y, ' ');
            }
        }

        return false;
    }

    public static boolean check(CharMap fullGrid, CharMap tile, int gridX, int gridY, int size) {
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++) {
                char c = tile.get(x, y);
                if ((x == 0) && (gridX > 0)) {
                    if (fullGrid.get(gridX * size - 1, gridY * size + y) != c) return false;
                }
                if ((y == 0) && (gridY > 0)) {
                    if (fullGrid.get(gridX * size + x, gridY * size - 1) != c) return false;
                }

                fullGrid.set(gridX * size + x, gridY * size + y, c);
            }

        return true;

    }

    public static CharMap flip(CharMap in) {
        List<Point2D> points = in.points();
        int wid = points.stream().mapToInt(Point2D::x).max().orElseThrow();

        CharMap m = new CharMap();
        for (Point2D p : points) {
            m.set(wid - p.x(), p.y(), in.get(p));
        }

        return m;
    }

    public static CharMap rotate(CharMap in) {
        List<Point2D> points = in.points();
        int wid = points.stream().mapToInt(Point2D::y).max().orElseThrow();

        CharMap m = new CharMap();
        for (Point2D p : points) {
            m.set(p.y(), wid - p.x(), in.get(p));
        }

        return m;
    }

    public static List<CharMap> directions(CharMap map) {
        List<CharMap> direction = new ArrayList<>();

        direction.add(map);

        CharMap m = map;
        for (int i = 0; i < 3; i++) {
            m = rotate(m);
            direction.add(m);
        }

        m = flip(map);
        direction.add(m);
        for (int i = 0; i < 3; i++) {
            m = rotate(m);
            direction.add(m);
        }

        return direction;
    }

    public static CharMap removeBorder(CharMap fin, int grid, int size) {
        CharMap map = new CharMap();
        int small = size - 2;

        for (int gx = 0; gx < grid; gx++) {
            for (int gy = 0; gy < grid; gy++) {
                for (int x = 0; x < small; x++) {
                    for (int y = 0; y < small; y++) {
                        char z = fin.get(gx * size + 1 + x, gy * size + 1 + y);
                        map.set(gx * small + x, gy * small + y, z);
                    }
                }
            }
        }

        LOGGER.info("Trim: \n{}", map);
        return map;
    }

    public static CharMap readTile(Scanner scan) {
        CharMap m = new CharMap();
        int y = 0;
        while (scan.hasNextLine()) {
            String line = scan.nextLine().trim();
            if (line.length() == 0) break;

            for (int x = 0; x < line.length(); x++) {
                m.set(x, y, line.charAt(x));
            }
            y++;

        }
        return m;
    }

    public static int countAndMark(CharMap seamonster, CharMap map) {
        int found = 0;

        for (Point2D p : map.points()) {
            boolean monster = true;
            for (Point2D q : seamonster.points()) {
                if (seamonster.get(q.x(), q.y()) == '#') {
                    char z = map.get(p.x() + q.x(), p.y() + q.y());
                    if ((z != '#') && (z != 'O')) {
                        monster = false;
                        break;

                    }
                }
            }
            if (monster) {
                for (Point2D q : seamonster.points()) {
                    if (seamonster.get(q.x(), q.y()) == '#') {
                        map.set(p.x() + q.x(), p.y() + q.y(), 'O');
                    }
                }

                found++;
            }


        }


        return found;

    }

    private static long findSeaMonster(CharMap seaMonster, CharMap trim) {
        for (CharMap f : directions(trim)) {
            int found = countAndMark(seaMonster, f);
            long sea = f.points().stream().filter(p -> f.get(p) == '#').count();
            LOGGER.debug("Found: {} sea: {}", found, sea);
            if (found > 0) {
                return sea;
            }
        }
        return 0;
    }

    private static long computeProduct(Map<Integer, List<CharMap>> tiles, int grid, CharMap fullGrid) {
        IntegerMap gridMap = new IntegerMap();
        assembleJurassicJigsaw(tiles, gridMap, 0, 0, new HashSet<>(), grid, 10, fullGrid);
        LOGGER.info("GridMap: \n{}", gridMap);

        long product = 1;
        product *= gridMap.get(0, 0);
        product *= gridMap.get(grid - 1, 0);
        product *= gridMap.get(0, grid - 1);
        product *= gridMap.get(grid - 1, grid - 1);

        LOGGER.info("Product: {}", product);
        return product;
    }

    @Test
    void testJurassicJigsaw() {
        Scanner scanner = new Scanner(
                """
                        Tile 2311:
                        ..##.#..#.
                        ##..#.....
                        #...##..#.
                        ####.#...#
                        ##.##.###.
                        ##...#.###
                        .#.#.#..##
                        ..#....#..
                        ###...#.#.
                        ..###..###

                        Tile 1951:
                        #.##...##.
                        #.####...#
                        .....#..##
                        #...######
                        .##.#....#
                        .###.#####
                        ###.##.##.
                        .###....#.
                        ..#.#..#.#
                        #...##.#..

                        Tile 1171:
                        ####...##.
                        #..##.#..#
                        ##.#..#.#.
                        .###.####.
                        ..###.####
                        .##....##.
                        .#...####.
                        #.##.####.
                        ####..#...
                        .....##...

                        Tile 1427:
                        ###.##.#..
                        .#..#.##..
                        .#.##.#..#
                        #.#.#.##.#
                        ....#...##
                        ...##..##.
                        ...#.#####
                        .#.####.#.
                        ..#..###.#
                        ..##.#..#.

                        Tile 1489:
                        ##.#.#....
                        ..##...#..
                        .##..##...
                        ..#...#...
                        #####...#.
                        #..#.#.#.#
                        ...#.#.#..
                        ##.#...##.
                        ..##.##.##
                        ###.##.#..

                        Tile 2473:
                        #....####.
                        #..#.##...
                        #.##..#...
                        ######.#.#
                        .#...#.#.#
                        .#########
                        .###.#..#.
                        ########.#
                        ##...##.#.
                        ..###.#.#.

                        Tile 2971:
                        ..#.#....#
                        #...###...
                        #.#.###...
                        ##.##..#..
                        .#####..##
                        .#..####.#
                        #..#.#..#.
                        ..####.###
                        ..#.#.###.
                        ...#.#.#.#

                        Tile 2729:
                        ...#.#.#.#
                        ####.#....
                        ..#.#.....
                        ....#..#.#
                        .##..##.#.
                        .#.####...
                        ####.#.#..
                        ##.####...
                        ##..#.##..
                        #.##...##.

                        Tile 3079:
                        #.#.#####.
                        .#..######
                        ..#.......
                        ######....
                        ####.#..#.
                        .#...#.##.
                        #.#####.##
                        ..#.###...
                        ..#.......
                        ..#.###...
                        """);

        Map<Integer, List<CharMap>> tiles = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            line = line.replace(":", "");

            tiles.put(Integer.parseInt(line.split(" ")[1]), directions(readTile(scanner)));
        }
        LOGGER.info("Read tiles: {}", tiles.size());

        int grid = (int) Math.round(Math.sqrt(tiles.size()));
        LOGGER.info("Grid size: {}", grid);

        CharMap fullGrid = new CharMap();
        long product = computeProduct(tiles, grid, fullGrid);
        assertThat(product).isEqualTo(20899048083289L);

        LOGGER.info("Solution: \n{}", fullGrid);

        CharMap trim = removeBorder(fullGrid, grid, 10);

        CharMap seaMonster = readSeaMonster();
        long monster = findSeaMonster(seaMonster, trim);
        assertThat(monster).isEqualTo(273L);
    }

    /**
     * --- Day 20: Jurassic Jigsaw ---
     * The high-speed train leaves the forest and quickly carries you south. You
     * can even see a desert in the distance! Since you have some spare time, you
     * might as well see if there was anything interesting in the image the
     * Mythical Information Bureau satellite captured.
     * <p>
     * After decoding the satellite messages, you discover that the data actually
     * contains many small images created by the satellite's camera array. The
     * camera array consists of many cameras; rather than produce a single square
     * image, they produce many smaller square image tiles that need to be
     * reassembled back into a single image.
     * <p>
     * Each camera in the camera array returns a single monochrome image tile with
     * a random unique ID number. The tiles (your puzzle input) arrived in a
     * random order.
     * <p>
     * Worse yet, the camera array appears to be malfunctioning: each image tile
     * has been rotated and flipped to a random orientation. Your first task is to
     * reassemble the original image by orienting the tiles so they fit together.
     * <p>
     * To show how the tiles should be reassembled, each tile's image data
     * includes a border that should line up exactly with its adjacent tiles. All
     * tiles have this border, and the border lines up exactly when the tiles are
     * both oriented correctly. Tiles at the edge of the image also have this
     * border, but the outermost edges won't line up with any other tiles.
     * <p>
     * For example, suppose you have the following nine tiles:
     * <p>
     * Tile 2311:
     * ..##.#..#.
     * ##..#.....
     * #...##..#.
     * ####.#...#
     * ##.##.###.
     * ##...#.###
     * .#.#.#..##
     * ..#....#..
     * ###...#.#.
     * ..###..###
     * <p>
     * Tile 1951:
     * #.##...##.
     * #.####...#
     * .....#..##
     * #...######
     * .##.#....#
     * .###.#####
     * ###.##.##.
     * .###....#.
     * ..#.#..#.#
     * #...##.#..
     * <p>
     * Tile 1171:
     * ####...##.
     * #..##.#..#
     * ##.#..#.#.
     * .###.####.
     * ..###.####
     * .##....##.
     * .#...####.
     * #.##.####.
     * ####..#...
     * .....##...
     * <p>
     * Tile 1427:
     * ###.##.#..
     * .#..#.##..
     * .#.##.#..#
     * #.#.#.##.#
     * ....#...##
     * ...##..##.
     * ...#.#####
     * .#.####.#.
     * ..#..###.#
     * ..##.#..#.
     * <p>
     * Tile 1489:
     * ##.#.#....
     * ..##...#..
     * .##..##...
     * ..#...#...
     * #####...#.
     * #..#.#.#.#
     * ...#.#.#..
     * ##.#...##.
     * ..##.##.##
     * ###.##.#..
     * <p>
     * Tile 2473:
     * #....####.
     * #..#.##...
     * #.##..#...
     * ######.#.#
     * .#...#.#.#
     * .#########
     * .###.#..#.
     * ########.#
     * ##...##.#.
     * ..###.#.#.
     * <p>
     * Tile 2971:
     * ..#.#....#
     * #...###...
     * #.#.###...
     * ##.##..#..
     * .#####..##
     * .#..####.#
     * #..#.#..#.
     * ..####.###
     * ..#.#.###.
     * ...#.#.#.#
     * <p>
     * Tile 2729:
     * ...#.#.#.#
     * ####.#....
     * ..#.#.....
     * ....#..#.#
     * .##..##.#.
     * .#.####...
     * ####.#.#..
     * ##.####...
     * ##..#.##..
     * #.##...##.
     * <p>
     * Tile 3079:
     * #.#.#####.
     * .#..######
     * ..#.......
     * ######....
     * ####.#..#.
     * .#...#.##.
     * #.#####.##
     * ..#.###...
     * ..#.......
     * ..#.###...
     * <p>
     * By rotating, flipping, and rearranging them, you can find a square
     * arrangement that causes all adjacent borders to line up:
     * <p>
     * #...##.#.. ..###..### #.#.#####.
     * ..#.#..#.# ###...#.#. .#..######
     * .###....#. ..#....#.. ..#.......
     * ###.##.##. .#.#.#..## ######....
     * .###.##### ##...#.### ####.#..#.
     * .##.#....# ##.##.###. .#...#.##.
     * #...###### ####.#...# #.#####.##
     * .....#..## #...##..#. ..#.###...
     * #.####...# ##..#..... ..#.......
     * #.##...##. ..##.#..#. ..#.###...
     * <p>
     * #.##...##. ..##.#..#. ..#.###...
     * ##..#.##.. ..#..###.# ##.##....#
     * ##.####... .#.####.#. ..#.###..#
     * ####.#.#.. ...#.##### ###.#..###
     * .#.####... ...##..##. .######.##
     * .##..##.#. ....#...## #.#.#.#...
     * ....#..#.# #.#.#.##.# #.###.###.
     * ..#.#..... .#.##.#..# #.###.##..
     * ####.#.... .#..#.##.. .######...
     * ...#.#.#.# ###.##.#.. .##...####
     * <p>
     * ...#.#.#.# ###.##.#.. .##...####
     * ..#.#.###. ..##.##.## #..#.##..#
     * ..####.### ##.#...##. .#.#..#.##
     * #..#.#..#. ...#.#.#.. .####.###.
     * .#..####.# #..#.#.#.# ####.###..
     * .#####..## #####...#. .##....##.
     * ##.##..#.. ..#...#... .####...#.
     * #.#.###... .##..##... .####.##.#
     * #...###... ..##...#.. ...#..####
     * ..#.#....# ##.#.#.... ...##.....
     * <p>
     * For reference, the IDs of the above tiles are:
     * <p>
     * 1951    2311    3079
     * 2729    1427    2473
     * 2971    1489    1171
     * <p>
     * To check that you've assembled the image correctly, multiply the IDs of the
     * four corner tiles together. If you do this with the assembled tiles from
     * the example above, you get 1951 * 3079 * 2971 * 1171 = 20899048083289.
     * <p>
     * Assemble the tiles into an image. What do you get if you multiply together
     * the IDs of the four corner tiles?
     * <p>
     * --- Part Two ---
     * <p>
     * Now, you're ready to check the image for sea monsters.
     * <p>
     * The borders of each tile are not part of the actual image; start by
     * removing them.
     * <p>
     * In the example above, the tiles become:
     * <p>
     * .#.#..#. ##...#.# #..#####
     * ###....# .#....#. .#......
     * ##.##.## #.#.#..# #####...
     * ###.#### #...#.## ###.#..#
     * ##.#.... #.##.### #...#.##
     * ...##### ###.#... .#####.#
     * ....#..# ...##..# .#.###..
     * .####... #..#.... .#......
     * <p>
     * #..#.##. .#..###. #.##....
     * #.####.. #.####.# .#.###..
     * ###.#.#. ..#.#### ##.#..##
     * #.####.. ..##..## ######.#
     * ##..##.# ...#...# .#.#.#..
     * ...#..#. .#.#.##. .###.###
     * .#.#.... #.##.#.. .###.##.
     * ###.#... #..#.##. ######..
     * <p>
     * .#.#.### .##.##.# ..#.##..
     * .####.## #.#...## #.#..#.#
     * ..#.#..# ..#.#.#. ####.###
     * #..####. ..#.#.#. ###.###.
     * #####..# ####...# ##....##
     * #.##..#. .#...#.. ####...#
     * .#.###.. ##..##.. ####.##.
     * ...###.. .##...#. ..#..###
     * <p>
     * Remove the gaps to form the actual image:
     * <p>
     * .#.#..#.##...#.##..#####
     * ###....#.#....#..#......
     * ##.##.###.#.#..######...
     * ###.#####...#.#####.#..#
     * ##.#....#.##.####...#.##
     * ...########.#....#####.#
     * ....#..#...##..#.#.###..
     * .####...#..#.....#......
     * #..#.##..#..###.#.##....
     * #.####..#.####.#.#.###..
     * ###.#.#...#.######.#..##
     * #.####....##..########.#
     * ##..##.#...#...#.#.#.#..
     * ...#..#..#.#.##..###.###
     * .#.#....#.##.#...###.##.
     * ###.#...#..#.##.######..
     * .#.#.###.##.##.#..#.##..
     * .####.###.#...###.#..#.#
     * ..#.#..#..#.#.#.####.###
     * #..####...#.#.#.###.###.
     * #####..#####...###....##
     * #.##..#..#...#..####...#
     * .#.###..##..##..####.##.
     * ...###...##...#...#..###
     * <p>
     * Now, you're ready to search for sea monsters! Because your image is
     * monochrome, a sea monster will look like this:
     * <p>
     * #
     * #    ##    ##    ###
     * #  #  #  #  #  #
     * <p>
     * When looking for this pattern in the image, the spaces can be anything;
     * only the # need to match. Also, you might need to rotate or flip your image
     * before it's oriented correctly to find sea monsters. In the above image,
     * after flipping and rotating it to the appropriate orientation, there are
     * two sea monsters (marked with O):
     * <p>
     * .####...#####..#...###..
     * #####..#..#.#.####..#.#.
     * .#.#...#.###...#.##.O#..
     * #.O.##.OO#.#.OO.##.OOO##
     * ..#O.#O#.O##O..O.#O##.##
     * ...#.#..##.##...#..#..##
     * #.##.#..#.#..#..##.#.#..
     * .###.##.....#...###.#...
     * #.####.#.#....##.#..#.#.
     * ##...#..#....#..#...####
     * ..#.##...###..#.#####..#
     * ....#.##.#.#####....#...
     * ..##.##.###.....#.##..#.
     * #...#...###..####....##.
     * .#.##...#.##.#.#.###...#
     * #.###.#..####...##..#...
     * #.###...#.##...#.##O###.
     * .O##.#OO.###OO##..OOO##.
     * ..O#.O..O..O.#O##O##.###
     * #.#..##.########..#..##.
     * #.#####..#.#...##..#....
     * #....##..#.#########..##
     * #...#.....#..##...###.##
     * #..###....##.#...##.##.#
     * <p>
     * Determine how rough the waters are in the sea monsters' habitat by counting
     * the number of # that are not part of a sea monster. In the above example,
     * the habitat's water roughness is 273.
     * <p>
     * How many # are not part of a sea monster?
     */
    @Test
    void inputJurassicJigsaw() {
        Scanner scanner = new Scanner(Day20Test.class.getResourceAsStream("/day/20/input"));

        Map<Integer, List<CharMap>> tiles = new HashMap<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            line = line.replace(":", "");

            tiles.put(Integer.parseInt(line.split(" ")[1]), directions(readTile(scanner)));
        }
        LOGGER.info("Read tiles: {}", tiles.size());

        int grid = (int) Math.round(Math.sqrt(tiles.size()));
        LOGGER.info("Grid size: {}", grid);

        CharMap fullGrid = new CharMap();
        long product = computeProduct(tiles, grid, fullGrid);
        assertThat(product).isEqualTo(16937516456219L);

        LOGGER.info("Solution: \n{}", fullGrid);

        CharMap trim = removeBorder(fullGrid, grid, 10);

        CharMap seaMonster = readSeaMonster();
        long monster = findSeaMonster(seaMonster, trim);
        assertThat(monster).isEqualTo(1858);
    }
}
