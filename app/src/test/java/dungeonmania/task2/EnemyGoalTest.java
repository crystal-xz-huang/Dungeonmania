package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyGoalTest {
    @Test
    @DisplayName("Test achieving a basic enemy goal when bomb explodes")
    public void bombExplode() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_bomb", "c_enemyGoalTest_bomb");

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // activate Switch
        res = dmc.tick(Direction.RIGHT);

        // pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());

        // place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        // check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());

        // check all enemies are dead
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast").size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
        assertEquals(0, TestUtils.getEntities(res, "assassin").size());
        assertEquals(0, TestUtils.getEntities(res, "hydra").size());

        // check player is still alive
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        // assert goal met (4 enemies)
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test allies destroyed during bomb explosion does not count towards enemy goal")
    public void alliesDestroyCount() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalTest_ally", "c_enemyGoalTest_ally");
        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();

        // assert goal not met (1 enemy)
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(mercId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // activate Switch
        res = dmc.tick(Direction.RIGHT);

        // pick up Bomb
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "bomb").size());

        // place Cardinally Adjacent
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(TestUtils.getInventory(res, "bomb").get(0).getId());

        // check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());

        // check all enemies are dead
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());

        // check player is still alive
        assertEquals(1, TestUtils.getEntities(res, "player").size());

        // assert not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
    }

    // @Test
    // @DisplayName("Test goal is only evaluated after the first tick")
    // public void noEnemies() throws IllegalArgumentException, InvalidActionException {
    //     DungeonManiaController dmc;
    //     dmc = new DungeonManiaController();
    //     // Enemy goal is 0 and there are no enemies
    //     DungeonResponse res = dmc.newGame("d_basicGoalsTest_noEnemies", "c_basicGoalsTest_noEnemies");

    //     // check that there are no enemies
    //     assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));
    //     assertEquals(1, TestUtils.countType(res, "player"));

    //     // assert goal not met
    //     assertTrue(TestUtils.getGoals(res).contains(":enemies"));

    //     // collect sword
    //     dmc.tick(Direction.DOWN);

    //     // assert goal met after first tick
    //     assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));
    //     assertEquals(1, TestUtils.countType(res, "player"));
    //     assertEquals("", TestUtils.getGoals(res));
    // }
}
