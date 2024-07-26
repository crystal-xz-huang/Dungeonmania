package dungeonmania.entities.enemies;

import java.util.Random;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    public static final int DEFAULT_BRIBE_AMOUNT = 2;
    public static final double DEFAULT_ATTACK = 8.0;
    public static final double DEFAULT_HEALTH = 12.0;
    public static final double DEFAULT_BRIBE_FAIL_RATE = 0.5;

    private Random random = new Random();
    private double bribeFailRate;

    public Assassin(Position position, BattleStatistics stats, int bribeAmount, int bribeRadius, double bribeFailRate,
            BattleStatistics allyStats) {
        super(position, stats, bribeAmount, bribeRadius, allyStats);
        this.bribeFailRate = bribeFailRate;
    }

    @Override
    public void interact(Player player, Game game) {
        if (canBeBribed(player)) {
            float randomFloat = random.nextFloat();
            player.bribe(getBribeAmount());
            if (randomFloat > bribeFailRate)
                setAllied(true);
        } else {
            useScepter(player, game);
        }
        moveAdjacent(player);
    }
}
