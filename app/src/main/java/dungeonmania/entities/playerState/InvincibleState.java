package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsDirector;
import dungeonmania.entities.Player;

public class InvincibleState extends PlayerState {
    public InvincibleState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics getStateStatistics() {
        return new BattleStatisticsDirector().constructInvincibilityStatistics();
    }
}
