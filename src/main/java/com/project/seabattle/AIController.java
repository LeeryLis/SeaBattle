package com.project.seabattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIController {

    private final Field field;

    private Coordinate lastSuccessAttack;
    private List<Coordinate> nextAttackList = new ArrayList<>();

    public AIController(Field field) {
        this.field = field;
    }

    public void startAttack() {
        if (lastSuccessAttack == null) {
            List<Coordinate> allowList = field.allowFireCells();

            Coordinate randomCoordinate = allowList.get(new Random().nextInt(allowList.size()));
            int x = randomCoordinate.getX();
            int y = randomCoordinate.getY();

            if (field.attackCell(x, y) && !field.isKill(x, y)) {
                lastSuccessAttack = new Coordinate(x, y);

                if (field.isAllowFire(x + 1, y)) nextAttackList.add(new Coordinate(x + 1, y));
                if (field.isAllowFire(x - 1, y)) nextAttackList.add(new Coordinate(x - 1, y));

                if (continueAttack()) startAttack();
            }
        }
        else {
            if (continueAttack()) startAttack();
        }
    }

    private boolean continueAttack() {
        if (nextAttackList.isEmpty()) {
            if (field.isAllowFire(lastSuccessAttack.getX(), lastSuccessAttack.getY() + 1))
                nextAttackList.add(new Coordinate(lastSuccessAttack.getX(), lastSuccessAttack.getY() + 1));
            if (field.isAllowFire(lastSuccessAttack.getX(), lastSuccessAttack.getY() - 1))
                nextAttackList.add(new Coordinate(lastSuccessAttack.getX(), lastSuccessAttack.getY() - 1));
        }

        if (field.attackCell(nextAttackList.get(0).getX(), nextAttackList.get(0).getY())) {
            if (field.isKill(nextAttackList.get(0).getX(), nextAttackList.get(0).getY())) {
                lastSuccessAttack = null;
                nextAttackList = new ArrayList<>();
                return true;
            }

            int newX = nextAttackList.get(0).getX();
            int newY = nextAttackList.get(0).getY();

            if (newX > lastSuccessAttack.getX()) newX++;
            if (newX < lastSuccessAttack.getX()) newX--;
            if (newY > lastSuccessAttack.getY()) newY++;
            if (newY < lastSuccessAttack.getY()) newY--;

            if (field.isAllowFire(newX, newY)) {
                Coordinate newCoordinate = new Coordinate(newX, newY);
                nextAttackList.add(newCoordinate);
            }

            nextAttackList.remove(0);
            continueAttack();
        }
        else nextAttackList.remove(0);
        return false;
    }
}
