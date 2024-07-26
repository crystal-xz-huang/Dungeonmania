package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

public class AssassinTest {
    @Test
    @DisplayName("Test successful bribe attempt")
    public void bribeSuccess() {
        //                                                 Wall     Wall     Wall    Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4      A4       A3       A2     A1      Wall
        //                                                 Wall     Wall     Wall    Wall    Wall
        // set bribe amount to 2
        // set bribe fail rate to 0 (never)
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribes", "c_assassinTest_bribeSuccess");

        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // move towards assassin and collect treasure
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // attempt bribe
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));

        // check treasure is used()
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
    }

    @Test
    @DisplayName("Test failed bribe attempt")
    public void bribeFail() {
        //                                                 Wall     Wall     Wall    Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4      A4       A3       A2     A1      Wall
        //                                                 Wall     Wall     Wall    Wall    Wall
        // set bribe amount to 2
        // set bribe fail rate to 1 (everytime)
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribes", "c_assassinTest_bribeFail");

        String assassinId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // move towards assassin and collect treasure
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // attempt bribe
        res = assertDoesNotThrow(() -> dmc.interact(assassinId));

        //check treasure is used
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // walk into mercenary, a battle occurs
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(0, res.getBattles().size());
    }

}
