package com.project.seabattle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FieldTests {

    @Test
    void getCellTest() {
        Field field = new Field();
        field.fillCell(0,0,Cell.SHIP);

        assertEquals(Cell.SHIP, field.getCell(0,0));
    }

}