package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
// import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SceptreTest {
    @Test
    @DisplayName("Test mercenary can be mindcontrolled with a sceptre")
    public void mindControlMercernary() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sceptreTest_Mercenary", "c_sceptreTest");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(9, 1), getMercPos(res));

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(2, 1), getPlayerPos(res));
        assertEquals(new Position(8, 1), getMercPos(res));
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(3, 1), getPlayerPos(res));
        assertEquals(new Position(7, 1), getMercPos(res));

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), getPlayerPos(res));
        assertEquals(new Position(6, 1), getMercPos(res));

        // Tick 0: Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
        assertEquals(new Position(4, 1), getPlayerPos(res));
        assertEquals(new Position(5, 1), getMercPos(res));

        // Ensure mercenary is still present
        assertTrue(TestUtils.getEntities(res, "mercenary").stream().anyMatch(e -> e.getId().equals(mercId)));

        // Tick 1: Player mind controls merc. Mercenary is mind controlled.
        assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(new Position(4, 1), getPlayerPos(res));
        assertEquals(new Position(5, 1), getMercPos(res));

        // Tick 2: Walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(5, 1), getPlayerPos(res));
        assertEquals(new Position(4, 1), getMercPos(res));

        // Tick 3: Walk into mercenary, a battle does not occur
        res = dmc.tick(Direction.LEFT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(4, 1), getPlayerPos(res));
        assertEquals(new Position(5, 1), getMercPos(res));

        // Tick 4: Mercenary is no longer mind controlled. Walk into mercenary. Battle occurs.
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(new Position(5, 1), getPlayerPos(res));
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private Position getMercPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "mercenary").get(0).getPosition();
    }
}
