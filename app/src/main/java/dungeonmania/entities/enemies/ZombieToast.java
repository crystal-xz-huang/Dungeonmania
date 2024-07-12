package dungeonmania.entities.enemies;

import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.movement.*;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
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
