package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import java.util.List;

public class HydraTest {
    @Test
    @DisplayName("Test hydra movement")
    public void movement() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_HydraTest_hydraMovement", "c_HydraTest_hydraMovement");
        assertEquals(1, getHydras(res).size());

        boolean hydraMoved = false;
        Position prevPosition = getHydraPos(res);
        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.UP);
            if (!prevPosition.equals(getHydraPos(res))) {
                hydraMoved = true;
                break;
            }
        }
        assertTrue(hydraMoved);
    }

    @Test
    @DisplayName("Test hydra cannot move through closed doors and walls")
    public void doorsAndWalls() {
        //  W   W   W   W
        //  P   W   Z   W
        //      W   D   W
        //          K
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_HydraTest_doorsAndWalls", "c_HydraTest_hydraMovement");
        assertEquals(1, getHydras(res).size());
        Position position = getHydraPos(res);
        res = dmc.tick(Direction.UP);
        assertEquals(position, getHydraPos(res));

    }

    @Test
    @DisplayName("Test hydra gains health")
    public void hydraGainHealth() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_HydraTest_hydraBattle", "c_HydraTest_hydraGainHealth");

        // Hydra has a health increase rate of 1, so it will always gain health
        // Hydra has a health increase amount of 10
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "hydra"));
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));

        // Simulate the battle
        DungeonResponse postBattleResponse = dmc.tick(Direction.RIGHT);
        List<BattleResponse> battles = postBattleResponse.getBattles();
        List<EntityResponse> entities = postBattleResponse.getEntities();

        // Check that the Hydra has won the battle
        assertEquals(1, TestUtils.countEntityOfType(entities, "hydra"));
        assertEquals(0, TestUtils.countEntityOfType(entities, "player"));

        // Check the hydra health increased by 10 each round
        BattleResponse battle = battles.get(0);
        assertEquals(10, battle.getRounds().get(0).getDeltaEnemyHealth(), 0.001);
    }

    @Test
    @DisplayName("Test hydra never gains health")
    public void neverGainHealth() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_HydraTest_hydraBattle", "c_HydraTest_hydraNeverGainHealth");

        // Hydra has a health increase rate of 0, so it will never gain health (will always take damage)
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "hydra"));
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));

        // Simulate the battle
        DungeonResponse postBattleResponse = dmc.tick(Direction.RIGHT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        List<EntityResponse> entities = postBattleResponse.getEntities();

        // Check that the player has won the battle
        assertEquals(0, TestUtils.countEntityOfType(entities, "hydra"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));

        // Check that the hydra has taken damage
        int hydraHealth = Integer
                .parseInt(TestUtils.getValueFromConfigFile("hydra_health", "c_HydraTest_hydraNeverGainHealth"));
        assertTrue(-battle.getRounds().get(0).getDeltaEnemyHealth() >= hydraHealth);
    }

    @Test
    @DisplayName("Test hydra always gains health but health increase amount is 0")
    public void takesNoDamage() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_HydraTest_hydraBattle", "c_HydraTest_hydraTakesNoDamage");

        // The Hydra will always gain health since rate = 1, and never take damage
        // The Hydra will defeat the Player
        assertEquals(1, TestUtils.getEntities(res, "hydra").size());
        DungeonResponse postBattleResponse = dmc.tick(Direction.RIGHT);
        BattleResponse battle = postBattleResponse.getBattles().get(0);
        List<EntityResponse> entities = postBattleResponse.getEntities();

        // Test that the Hydra has won the battle
        assertEquals(1, TestUtils.countEntityOfType(entities, "hydra"));
        assertEquals(0, TestUtils.countEntityOfType(entities, "player"));

        // Hydra should not take any damage
        assertEquals(0, battle.getRounds().get(0).getDeltaEnemyHealth(), 0.001);
    }

    private List<EntityResponse> getHydras(DungeonResponse res) {
        return TestUtils.getEntities(res, "hydra");
    }

    private Position getHydraPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "hydra").get(0).getPosition();
    }
}
