package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.Direction;

public class MidnightArmourTest {
    @Test
    @DisplayName("Test building a Midnight Armour with zombies")
    public void testBuildMidnightArmourWithZombies() throws InvalidActionException {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombies", "c_midnightArmourTest_zombies");

        // Pick up Sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up SunStone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Check that there are zombies
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        // Build Midnight Armour with zombies present
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials still present in inventory
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test player kills zombies in 5 rounds without Midnight Armour")
    public void testNoMidnightArmour() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_default", "c_default");
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        res = dmc.tick(Direction.DOWN);
        assertEquals(1, res.getBattles().size());

        // Verify that one battle has 5 rounds
        BattleResponse battleResponse = res.getBattles().get(0);
        assertEquals(5, battleResponse.getRounds().size());

        // Verify that the player has killed the zombie
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
    }

    @Test
    @DisplayName("Test Midnight Armour defence buff negates zombie attacks")
    public void testNoMidnightArmourDefenceBuff() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombie_spawn", "c_midnightArmourTest_defence");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour successfully
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Zombie spawns
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        // Move right, battle occurs but Midnight Armour negates the zombie attack for 5 rounds
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        BattleResponse battleResponse = res.getBattles().get(0);
        assertEquals(5, battleResponse.getRounds().size());
        double initialPlayerHealth = battleResponse.getInitialPlayerHealth();

        // Verify that the player has killed the zombie
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        // Calculate final player health after all rounds
        double finalPlayerHealth = calculateFinalHealth(initialPlayerHealth, battleResponse.getRounds(), true);

        // Verify that player takes no damage
        assertEquals(initialPlayerHealth, finalPlayerHealth, 0.001);
    }

    @Test
    @DisplayName("Test Midnight Armour attack buff kills zombies in 1 round")
    public void testMidnightArmourAttackBuff() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombie_spawn", "c_midnightArmourTest_attack");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour successfully
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Zombie spawns
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        // Move right, battle occurs and player kills the zombie in 1 round
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        BattleResponse battleResponse = res.getBattles().get(0);
        assertEquals(1, battleResponse.getRounds().size());
        double initialPlayerHealth = battleResponse.getInitialPlayerHealth();

        // Verify that the player has killed the zombie
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        // Calculate final player health after all rounds
        double finalPlayerHealth = calculateFinalHealth(initialPlayerHealth, battleResponse.getRounds(), true);

        // Verify that player thats 0.6 damage without defence buff
        assertEquals(4.4, finalPlayerHealth, 0.001);
    }

    @Test
    @DisplayName("Test Midnight Armour lasts forever")
    public void testMidnightArmourLastsForever() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombie_spawn", "c_midnightArmourTest_durability");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sunstone
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour successfully
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Zombie spawns
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

        // Move right, battle occurs
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());

        // Keep moving down
        for (int i = 0; i < 100; i++) {
            res = dmc.tick(Direction.DOWN);
            assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));
        }

        // Verify that the player is still alive
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));

        // Verify that the player takes no damage due to Midnight Armour
        BattleResponse battleResponse = res.getBattles().get(0);
        double initialPlayerHealth = battleResponse.getInitialPlayerHealth();
        double finalPlayerHealth = calculateFinalHealth(initialPlayerHealth, battleResponse.getRounds(), true);
        assertEquals(initialPlayerHealth, finalPlayerHealth, 0.001);

        // Verify that the Midnight Armour is still in the inventory
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    }

    private double calculateFinalHealth(double initialHealth, List<RoundResponse> rounds, boolean isPlayer) {
        double finalHealth = initialHealth;
        for (RoundResponse round : rounds) {
            if (isPlayer) {
                finalHealth += round.getDeltaCharacterHealth();
            } else {
                finalHealth += round.getDeltaEnemyHealth();
            }
        }
        return finalHealth;
    }

}
