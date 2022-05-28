package com.project.seabattle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    //region FXML variables
    @FXML
    private Button buttonStart;

    @FXML
    private Button buttonClear;
    @FXML
    private AnchorPane pane;

    @FXML
    private Button buttonRotation;

    @FXML
    private Button buttonSize;

    @FXML
    private Button buttonQuickPlacement;

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
    //endregion

    private int gamePhase = 0;

    private Field fieldPlayer = new Field(fieldPlayerView);
    private Field fieldAI = new Field(fieldAIView);

    private final List<Ship> shipList = new ArrayList<>();

    private AIController aiController = new AIController(fieldPlayer, fieldPlayerView);

    //region start phase
    public void setShipList() {
        shipList.clear();

        shipList.add(new Ship(true, 4, 1));
        shipList.add(new Ship(true, 3, 2));
        shipList.add(new Ship(true, 2, 3));
        shipList.add(new Ship(true, 1, 4));
    }

    private void newGame() {
        setShipList();

        fieldPlayerView.setGridLinesVisible(false);
        fieldPlayerView.getChildren().clear();
        fieldPlayerView.setGridLinesVisible(true);

        fieldAIView.setGridLinesVisible(false);
        fieldAIView.getChildren().clear();
        fieldAIView.setGridLinesVisible(true);

        fieldPlayer.clear();
        fieldAI.clear();

        addTiles(fieldPlayerView);
        addTiles(fieldAIView);
    }

    private void randomShipPlacement(Field field, GridPane gridPane, boolean isVisible) {
        for (Ship ship: shipList) {
            while (ship.count > 0) {
                if (new Random().nextInt(2) == 0) ship.changeRotation();

                List<Coordinate> allowList = field.allowPlaceCells(ship.size, ship.isHorisontal);
                Coordinate randomCoordinate = allowList.get(new Random().nextInt(allowList.size()));

                for (int i = 0; i < ship.size; i++) {
                    field.fillCell(randomCoordinate.getX() + (ship.isHorisontal ? i : 0),
                            randomCoordinate.getY() + (ship.isHorisontal ? 0 : i),
                            Cell.SHIP, isVisible);
                }

                ship.count--;
            }
        }
    }

    @FXML
    void clearField() {
        newGame();

        gamePhase = 0;
    }

    @FXML
    void quickPlacement() {
        newGame();

        randomShipPlacement(fieldPlayer, fieldPlayerView, true);

        gamePhase = 1;
    }
    //endregion

    @FXML
    void startGame() {
        if (gamePhase == 1) {
            gamePhase = 2;

            pane.getChildren().remove(buttonRotation);
            pane.getChildren().remove(buttonSize);
            pane.getChildren().remove(buttonQuickPlacement);
            pane.getChildren().remove(textCount);
            pane.getChildren().remove(textRotation);
            pane.getChildren().remove(textSize);
            pane.getChildren().remove(buttonStart);
            pane.getChildren().remove(buttonClear);

            setShipList();
            randomShipPlacement(fieldAI, fieldAIView, false);

            aiController = new AIController(fieldPlayer, fieldPlayerView);
        }
    }

    private void clickField(int x, int y, boolean isPlayer) {
        if (gamePhase == 0 && isPlayer) placeShip();

        if (gamePhase == 2 && !isPlayer && fieldAI.isAllowFire(x, y)) {
            if (!fieldAI.attackCell(x, y)) {
                gamePhase = 3;
                aiController.startAttack();

                if (fieldPlayer.checkWin()) endGame(false);
                else gamePhase = 2;
            }
            else if (fieldAI.checkWin()) endGame(true);
        }
    }

    private void endGame(boolean isPlayerWin) {
        if (isPlayerWin) System.out.println("You win");
        else System.out.println("You lose");
        gamePhase = 4;
    }

    private void placeShip() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTiles(fieldPlayerView);
        addTiles(fieldAIView);

        fieldPlayer = new Field(fieldPlayerView);
        fieldAI = new Field(fieldAIView);
    }

    private void addTiles(GridPane gridPane) {
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {
                Tile tile = new Tile(i, j, gridPane == fieldPlayerView);
                tile.setPrefSize(20, 20);
                gridPane.add(tile, i, j);
            }
        }
    }

    class Tile extends Pane {
        private final int x;
        private final int y;
        private final boolean isPlayer;

        public Tile(int x, int y, boolean isPlayer) {
            this.x = x;
            this.y = y;
            this.isPlayer = isPlayer;
            setOnMouseClicked(e -> clickField(this.x, this.y, this.isPlayer));
        }
    }
}