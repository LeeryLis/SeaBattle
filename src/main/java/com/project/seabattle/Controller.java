package com.project.seabattle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    @FXML
    private AnchorPane pane;

    @FXML
    private Button buttonRotation;

    @FXML
    private Button buttonSize;

    @FXML
    private Button buttonQuickStart;

    @FXML
    private GridPane fieldAIView;

    @FXML
    private GridPane fieldPlayerView;

    @FXML
    private Text textCount;

    @FXML
    private Text textRotation;

    @FXML
    private Text textSize;


    private int gamePhase;

    private Field fieldPlayer = new Field();
    private Field fieldAI = new Field();

    private List<Ship> shipList = new ArrayList<>();


    private void newGame() {
        shipList.clear();

        shipList.add(new Ship(true, 4, 1));
        shipList.add(new Ship(true, 3, 2));
        shipList.add(new Ship(true, 2, 3));
        shipList.add(new Ship(true, 1, 4));

        fieldPlayer.clear();
        fieldAI.clear();
    }

    public Field randomShipPlacement() {
        newGame();

        for (Ship ship: shipList) {
            while (ship.count > 0) {
                if (new Random().nextInt(2) == 0) ship.changeRotation();

                List<Coordinate> allowList = fieldPlayer.allowCells(ship);
                Coordinate randomCoordinate = allowList.get(new Random().nextInt(allowList.size()));

                for (int i = 0; i < ship.size; i++) {
                    fieldPlayer.fillCell(randomCoordinate.getX() + (ship.isHorisontal ? i : 0),
                            randomCoordinate.getY() + (ship.isHorisontal ? 0 : i),
                            Cell.SHIP);

                    Rectangle rectangle = new Rectangle(20, 20);
                    rectangle.setFill(Paint.valueOf("#547da6"));
                    fieldPlayerView.add(rectangle,
                            randomCoordinate.getX() + (ship.isHorisontal ? i : 0),
                            randomCoordinate.getY() + (ship.isHorisontal ? 0 : i));
                }

                ship.count--;
            }
        }

        return fieldPlayer;
    }


    @FXML
    void quickStart() {
        newGame();

        pane.getChildren().remove(buttonRotation);
        pane.getChildren().remove(buttonSize);
        pane.getChildren().remove(buttonQuickStart);
        pane.getChildren().remove(textCount);
        pane.getChildren().remove(textRotation);
        pane.getChildren().remove(textSize);

        randomShipPlacement();
    }
}