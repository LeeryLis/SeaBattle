package com.project.seabattle;

import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.Random;

public class AIController {

    private final Field field;
    private final GridPane fieldView;

    public AIController(Field field, GridPane fieldView) {
        this.field = field;
        this.fieldView = fieldView;
    }

    public void startAttack() {
        List<Coordinate> allowList = field.allowFireCells();

        Coordinate randomCoordinate = allowList.get(new Random().nextInt(allowList.size()));
        int x = randomCoordinate.getX();
        int y = randomCoordinate.getY();

        field.attackCell(x, y);
    }

    private void attack(int x, int y) {

    }
}
