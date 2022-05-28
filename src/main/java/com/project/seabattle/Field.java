package com.project.seabattle;

import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Field {
    private final HashMap<Coordinate, Cell> field = new HashMap<>();
    private int shipwreckCount = 0;
    private final GridPane fieldView;

    public Field(GridPane fieldView) {
        this.fieldView = fieldView;
    }

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

    public void fillCell(int x, int y, Cell cell, boolean isVisible) {
        if (isVisible && (x >= 0 && y >= 0 && x < 10 && y < 10)) {
            if (cell == Cell.SHIP) {
                Rectangle rectangle = new Rectangle(20, 20);
                rectangle.setFill(Paint.valueOf(Color.SHIP));
                fieldView.add(rectangle, x, y);
            }
            else if (cell == Cell.SHIPWRECK) {
                Rectangle rectangle = new Rectangle(20, 20);
                rectangle.setFill(Paint.valueOf(Color.SHIPWRECK));
                fieldView.add(rectangle, x, y);
            }
            else if (cell == Cell.MISS) {
                Circle circle = new Circle(5);
                circle.setFill(Paint.valueOf(Color.MISS));
                fieldView.add(circle, x, y);
                GridPane.setHalignment(circle, HPos.CENTER);
            }
        }

        field.put(new Coordinate(x, y), cell);
    }

    public List<Coordinate> allowPlaceCells(int size, boolean isHorisontal) {
        List<Coordinate> result = new ArrayList<>();

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (isAllowPlaceShip(size, isHorisontal, x, y)) result.add(new Coordinate(x, y));
            }
        }

        return result;
    }

    public boolean isAllowPlaceShip(int size, boolean isHorisontal, int x, int y) {
        for (int i = 0; i < size; i++) {
            if (isHorisontal) {
                if (!isFreeCell(x + i, y)) {
                    return false;
                }
            }
            else {
                if (!isFreeCell(x, y + i)) {
                    return false;
                }
            }
        }

        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < size + 1; i++) {
                if (isHorisontal) {
                    if (field.containsKey(new Coordinate(x + i, y + j))) {
                        return false;
                    }
                }
                else {
                    if (field.containsKey(new Coordinate(x + j, y + i))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public List<Coordinate> allowFireCells() {
        List<Coordinate> result = new ArrayList<>();

        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                if (isAllowFire(x, y))
                    result.add(new Coordinate(x, y));

        return result;
    }

    public boolean attackCell(int x, int y) {
        if (field.get(new Coordinate(x, y)) == Cell.SHIP) {
            fillCell(x, y, Cell.SHIPWRECK, true);
            shipwreckCount++;

            if (isKill(x, y)) fillKilledArea(x, y);

            return true;
        }
        else {
            fillCell(x, y, Cell.MISS, true);

            return false;
        }
    }

    public boolean isKill(int x, int y) {
        return checkDirection(x, y, 1, 0) &&
                checkDirection(x, y, -1, 0) &&
                checkDirection(x, y, 0, 1) &&
                checkDirection(x, y, 0, -1);
    }

    private boolean checkDirection(int x, int y, int dx, int dy) {
        int i = 1;
        while (field.get(new Coordinate(x + dx * i, y + dy * i)) != Cell.SHIP) {
            if (!field.containsKey(new Coordinate(x + dx * i, y + dy * i)) || field.get(new Coordinate(x + dx * i, y + dy * i)) == Cell.MISS) return true;
            i++;
        }

        return false;
    }

    private void fillKilledArea(int x, int y) {
        fillDirection(x, y, 1, 0);
        fillDirection(x, y, -1, 0);
        fillDirection(x, y, 0, 1);
        fillDirection(x, y, 0, -1);
    }

    private void fillDirection(int x, int y, int dx, int dy) {
        int i = 0;
        while (field.get(new Coordinate(x + dx * i, y + dy * i)) == Cell.SHIPWRECK) {
            for (int b = -1; b < 2; b++)
                for (int a = -1; a < 2; a++)
                    if (!field.containsKey(new Coordinate(x + dx * i + a, y + dy * i + b)))
                        fillCell(x + dx * i + a, y + dy * i + b, Cell.MISS, true);
            i++;
        }
    }

    public boolean checkWin() {
        return shipwreckCount == 20;
    }
}
