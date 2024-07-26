package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SunStoneTest {
    @Test
    @DisplayName("Test sunstones count towards treasure goal")
    public void testSunstoneWithTreasureGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_treasureGoal", "c_sunstoneTest_treasureGoal");

        // goal is to collect 3 treasures
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(3, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test both sunstone and treasure count towards treasure goal")
    public void testSunstoneTreasureWithTreasureGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_treasureGoal", "c_sunstoneTest_treasureGoal");

        // goal is to collect 3 treasures
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // pick up sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test player can use a sun stone to open and walk through a door")
    public void testUseSunstoneWalkThroughOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_openDoor", "c_default");

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // walk through door and check sunstone is still there
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // walk through another door and check sunstone is still there
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // walk back and check door is still open
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // walk back and check door is still open
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // back to starting position
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @DisplayName("Test mercernary cannot be bribed with sun stone")
    public void testBribeMercWithSunstone() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_sunstoneTest_mercernary", "c_default");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // pick up sun_stone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(mercId));
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }
}
