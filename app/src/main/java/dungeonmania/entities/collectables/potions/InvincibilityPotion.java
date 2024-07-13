package dungeonmania.entities.collectables.potions;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvincibilityPotion(Position position, int duration) {
        super(position, duration);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setInvincible(true).build();
    }
}
