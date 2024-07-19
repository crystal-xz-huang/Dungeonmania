package dungeonmania.entities.collectables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.util.Position;

public class Sword extends BattleItem {
    public static final double DEFAULT_ATTACK = 1;
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 1;
    public static final int DEFAULT_DURABILITY = 5;
    public static final double DEFAULT_DEFENCE = 0;
    public static final double DEFAULT_DEFENCE_SCALE_FACTOR = 1;

    public Sword(Position position, int durability, BattleStatistics buff) {
        super(position, durability, buff);
    }

    @Override
    public boolean isWeapon() {
        return true;
    }
}
