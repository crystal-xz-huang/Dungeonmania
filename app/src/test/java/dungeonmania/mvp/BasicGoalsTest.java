package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicGoalsTest {
    @Test
    @Tag("13-1")
    @DisplayName("Test achieving a basic exit goal")
    public void exit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_exit", "c_basicGoalsTest_exit");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to exit
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-2")
    @DisplayName("Test achieving a basic boulders goal")
    public void oneSwitch() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_oneSwitch", "c_basicGoalsTest_oneSwitch");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move boulder onto switch
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-3")
    @DisplayName("Test achieving a boulders goal where there are five switches")
    public void fiveSwitches() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_fiveSwitches", "c_basicGoalsTest_fiveSwitches");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move first four boulders onto switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":boulders"));

        // move last boulder onto switch
        res = dmc.tick(Direction.DOWN);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-4")
    @DisplayName("Test achieving a basic treasure goal")
    public void treasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_treasure", "c_basicGoalsTest_treasure");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        // collect treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-5")
    @DisplayName("Test achieving a basic enemy goal with spider")
    public void enemySpider() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemySpider", "c_basicGoalsTest_enemySpider");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // attack spider
        res = dmc.tick(Direction.RIGHT);

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @Tag("13-6")
    @DisplayName("Test achieving a basic enemy goal with spawner")
    public void enemySpawner() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_basicGoalsTest_enemySpawner", "c_basicGoalsTest_enemySpawner");
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // collect sword
        dmc.tick(Direction.DOWN);

        // go under spawner
        dmc.tick(Direction.RIGHT);

        // destroy spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));

        // ensure that there are no spawners
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    // @Test
    // @Tag("13-7")
    // @DisplayName("Test goal is only evaluated after the first tick")
    // public void noEnemies() throws IllegalArgumentException, InvalidActionException {
    //     DungeonManiaController dmc;
    //     dmc = new DungeonManiaController();
    //     // Enemy goal is 0 and there are no enemies
    //     DungeonResponse res = dmc.newGame("d_basicGoalsTest_noEnemies", "c_basicGoalsTest_enemySpawner");

    //     // check that there are no enemies
    //     assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));
    //     assertEquals(1, TestUtils.countType(res, "player"));

    //     // assert goal not met
    //     assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    //     // collect sword
    //     dmc.tick(Direction.DOWN);

    //     // assert goal met after first tick
    //     assertEquals("", TestUtils.getGoals(res));
    // }
}
