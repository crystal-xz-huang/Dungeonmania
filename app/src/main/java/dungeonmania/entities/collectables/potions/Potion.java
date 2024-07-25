package dungeonmania.entities.collectables.potions;

import dungeonmania.util.Position;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;

public abstract class Potion extends BattleItem {
    private static final int DEFAULT_DURABILITY = 1;
    private int duration;

    public Potion(Position position, int duration, BattleStatistics buff) {
        super(position, DEFAULT_DURABILITY, buff);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public boolean isWeapon() {
        return false;
    }
}
