package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.entities.Player;

public class InvincibleState extends PlayerState {
    public InvincibleState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setInvincible(true).build();
    }
}
