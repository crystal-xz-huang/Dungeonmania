package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public abstract class PlayerState {
    private Player player;

    PlayerState(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, getBattleStatistics());
    }

    public abstract BattleStatistics getBattleStatistics();

}
