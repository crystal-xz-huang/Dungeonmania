package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsDirector;
import dungeonmania.entities.Player;

public class BaseState extends PlayerState {
    public BaseState(Player player) {
        super(player);
    }

    @Override
    public BattleStatistics getStateStatistics() {
        return new BattleStatisticsDirector().constructBaseStatistics();
    }
}
