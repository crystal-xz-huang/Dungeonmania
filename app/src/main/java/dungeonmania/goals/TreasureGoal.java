package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal implements Goal {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null || game.getTick() == 0)
            return false;
        return game.getCollectedTreasureCount() >= target;
    }

    @Override
    public String toString(Game game) {
        return (this.achieved(game) ? "" : ":treasure");
    }
}
