package com.project.seabattle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTests {

    @Test
    void RandomShipPlacementTest() {
        assertEquals(new Field(), new Controller().randomShipPlacement());
    }

}
