package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.entities.Player;

public class InvisibleState extends PlayerState {
    public InvisibleState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setEnabled(false).build();
    }
}
