package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;

public class Shield extends Buildable {
    private double defence;

    public Shield(int durability, double defence) {
        super(null, durability);
        this.defence = defence;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setDefence(defence).build();
    }
}
