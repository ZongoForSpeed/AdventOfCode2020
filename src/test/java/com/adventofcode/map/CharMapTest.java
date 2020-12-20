package com.adventofcode.map;

import org.junit.jupiter.api.Test;

import java.util.List;

class CharMapTest {
    @Test
    void testCharMap() {
        List<String> input = List.of(
                "                  # ",
                "#    ##    ##    ###",
                " #  #  #  #  #  #   "
        );

        CharMap map = new CharMap();

        for (int i = 0; i < input.size(); i++) {
            char[] line = input.get(i).toCharArray();
            for (int j = 0; j < line.length; j++) {
                if (line[j] == '#') {
                    map.set(j, i, line[j]);
                }
            }
        }

        System.out.println(map.toString());
    }

}