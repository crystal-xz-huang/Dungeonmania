package dungeonmania.goals;

import dungeonmania.Game;

// Component Interface
public interface Goal {
    public boolean achieved(Game game);

    public String toString(Game game);
}
