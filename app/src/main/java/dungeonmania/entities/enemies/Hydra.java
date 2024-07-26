package dungeonmania.entities.enemies;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.movement.FleeMovement;
import dungeonmania.entities.movement.MovementStrategy;
import dungeonmania.entities.movement.RandomMovement;
import dungeonmania.util.Position;

public class Hydra extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    public static final double DEFAULT_HEALTH_INCREASE_RATE = 0.5;
    public static final double DEFAULT_HEALTH_INCREASE_AMOUNT = 1;

    public Hydra(Position position, BattleStatistics stats) {
        super(position, stats);
    }

    @Override
    public MovementStrategy getMovementStrategy(Player player) {
        if (player.getEffectivePotion() instanceof InvincibilityPotion) {
            return new FleeMovement();
        } else {
            return new RandomMovement();
        }
    }
}
