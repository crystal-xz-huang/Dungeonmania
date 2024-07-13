package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;

public class Bow extends Buildable {
    public Bow(int durability) {
        super(null, durability);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setMagnifier(2).build();
    }

}
