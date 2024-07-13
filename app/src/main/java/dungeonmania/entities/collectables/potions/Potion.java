package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.BattleItem;
import dungeonmania.util.Position;

public abstract class Potion extends BattleItem {
    private int duration;

    public Potion(Position position, int duration) {
        super(position, 1);
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
