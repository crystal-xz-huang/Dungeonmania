package dungeonmania.goals;

import dungeonmania.Game;

public abstract class Goal {
    public boolean achieved(Game game) {
        return game.getPlayer() == null;
    }

    public abstract String toString(Game game);
}
