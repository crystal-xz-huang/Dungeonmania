package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.Switch;

public class BoulderGoal implements Goal {
    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null || game.getTick() == 0)
            return false;
        return game.getMap().getEntities(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    @Override
    public String toString(Game game) {
        return (this.achieved(game) ? "" : ":boulders");
    }
}
