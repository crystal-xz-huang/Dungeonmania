package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexGoalsTest {
    @Test
    @Tag("14-1")
    @DisplayName("Testing a map with 4 conjunction goal")
    public void andAll() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_andAll", "c_complexGoalsTest_andAll");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // kill spider
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // pickup treasure
        res = dmc.tick(Direction.DOWN);
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // move to exit
        res = dmc.tick(Direction.DOWN);
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("14-2")
    @DisplayName("Testing a map with 4 disjunction goal")
    public void orAll() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_orAll", "c_complexGoalsTest_orAll");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move onto exit
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("14-3")
    @DisplayName("Testing that the exit goal must be achieved last in EXIT and TREASURE")
    public void exitAndTreasureOrder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_exitAndTreasureOrder",
                "c_complexGoalsTest_exitAndTreasureOrder");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // move player onto exit
        res = dmc.tick(Direction.RIGHT);

        // don't check state of exit goal in string
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // move player to pick up treasure
        res = dmc.tick(Direction.RIGHT);

        // assert treasure goal met, but goal string is not empty
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertNotEquals("", TestUtils.getGoals(res));

        // move player back onto exit
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("14-4")
    @DisplayName("Testing that the exit goal must be achieved last and EXIT and TREASURE")
    public void exitAndBouldersAndTreasureOrder() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_exitAndBouldersAndTreasureOrder",
                "c_complexGoalsTest_exitAndBouldersAndTreasureOrder");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move player onto treasure
        res = dmc.tick(Direction.RIGHT);

        // assert treasure goal met
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));

        // move player onto exit
        res = dmc.tick(Direction.RIGHT);

        // assert treasure goal remains achieved
        // don't check state of exit goal in string
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));

        // move boulder onto switch, but goal string is not empty
        res = dmc.tick(Direction.RIGHT);
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));
        assertFalse(TestUtils.getGoals(res).contains(":treasure"));
        assertNotEquals("", TestUtils.getGoals(res));

        // move back onto exit
        res = dmc.tick(Direction.LEFT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("14-6")
    @DisplayName("Testing a switch goal can be achieved and then become unachieved")
    public void switchUnachieved() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_switchUnachieved", "c_complexGoalsTest_switchUnachieved");

        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);

        // assert boulder goal met
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertFalse(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder off switch
        res = dmc.tick(Direction.RIGHT);

        // assert boulder goal unmet
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));
    }

    @Test
    @Tag("14-6")
    @DisplayName("Testing an enemy goal is achieved when both spawners and required enemies are destroyed")
    public void enemiesAndSpawnersDestroyed() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_enemies", "c_complexGoalsTest_enemies");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // attack spider
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // collect sword
        dmc.tick(Direction.LEFT);
        dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);

        //destroy spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        //ensure that there are no enemies
        assertEquals(0, TestUtils.countType(res, "spider"));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @Tag("14-7")
    @DisplayName("Testing an enemy goal is not achieved if the required enemies are not destroyed")
    public void enemiesNotDestroyed() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_enemies", "c_complexGoalsTest_enemies");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // collect sword
        res = dmc.tick(Direction.DOWN);

        //destroy spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        //ensure that there are no spawners
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        //ensure that there is an enemy
        assertEquals(1, TestUtils.countType(res, "spider"));

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    }

    @Test
    @Tag("14-8")
    @DisplayName("Testing an enemy goal is not achieved if all spawners are not destroyed")
    public void spawnersNotDestroyed() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_complexGoalsTest_enemies", "c_complexGoalsTest_enemies");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // attack spider
        dmc.tick(Direction.RIGHT);
        dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        //ensure that there are no enemies
        assertEquals(0, TestUtils.countType(res, "spider"));

        // assert goal not met
        assertEquals(":enemies", TestUtils.getGoals(res));

    }
}
