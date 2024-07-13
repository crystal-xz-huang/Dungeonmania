package dungeonmania.entities.collectables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.entities.BattleItem;
import dungeonmania.util.Position;

public class Sword extends BattleItem {
    public static final double DEFAULT_ATTACK = 1;
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 1;
    public static final int DEFAULT_DURABILITY = 5;
    public static final double DEFAULT_DEFENCE = 0;
    public static final double DEFAULT_DEFENCE_SCALE_FACTOR = 1;

    private double attack;

    public Sword(Position position, double attack, int durability) {
        super(position, durability);
        this.attack = attack;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setAttack(attack).build();
    }

    @Override
    public boolean isWeapon() {
        return true;
    }
}
