package com.project.seabattle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Field {
    private HashMap<Coordinate, Cell> field = new HashMap<>();

    public void clear() {
        field.clear();
    }

    public boolean isFreeCell(int x, int y) {
        return (x >= 0 && y >= 0 && x < 10 && y < 10) && !field.containsKey(new Coordinate(x, y));
    }

    public boolean isAllowFire(int x, int y) {
        return (x >= 0 && y >= 0 && x < 10 && y < 10) &&
                (!field.containsKey(new Coordinate(x, y)) || field.get(new Coordinate(x, y)) == Cell.SHIP);
    }

    public void fillCell(int x, int y, Cell cell) {
        field.put(new Coordinate(x, y), cell);
    }

    public Cell getCell(int x, int y) {
        return field.get(new Coordinate(x, y));
    }

    public List<Coordinate> allowCells(Ship ship) {
        List<Coordinate> result = new ArrayList<>();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                boolean isAllow = true;

                for (int j = -1; j < 2; j++) {
                    for (int i = -1; i < ship.size + 1; i++) {
                        if (ship.isHorisontal) {
                            if (field.containsKey(new Coordinate(x + i, y + j))) {
                                isAllow = false;
                                break;
                            }
                        }
                        else {
                            if (field.containsKey(new Coordinate(x + j, y + i))) {
                                isAllow = false;
                                break;
                            }
                        }
                    }
                }

                if (isAllow) result.add(new Coordinate(x, y));
            }
        }

        return result;
    }
}
