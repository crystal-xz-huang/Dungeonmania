package dungeonmania.entities.collectables.potions;

import dungeonmania.util.Position;
import dungeonmania.battles.BattleStatistics;

public class InvisibilityPotion extends Potion {
    public static final int DEFAULT_DURATION = 8;

    public InvisibilityPotion(Position position, int duration, BattleStatistics buff) {
        super(position, duration, buff);
    }
}
