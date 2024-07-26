package dungeonmania.battles;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

/**
 * Entities implement this interface can do battles
 */
public abstract class Battleable extends Entity {
    private BattleStatistics battleStatistics;

    public Battleable(Position position, BattleStatistics stats) {
        super(position);
        this.battleStatistics = stats;
    }

    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public double getHealth() {
        return battleStatistics.getHealth();
    }

    public void setHealth(double health) {
        battleStatistics.setHealth(health);
    }

    public void setBattleStatistics(BattleStatistics battleStatistics) {
        this.battleStatistics = battleStatistics;
    }
}
