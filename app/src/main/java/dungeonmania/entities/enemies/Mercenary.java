package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.Observer;
import dungeonmania.battles.*;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.movement.*;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable, Observer {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;
    private int mindControlDuration = 0;
    private boolean isValid = true;

    private BattleStatistics allyStats;

    public Mercenary(Position position, BattleStatistics stats, int bribeAmount, int bribeRadius,
            BattleStatistics allyStats) {
        super(position, stats);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyStats = allyStats;
    }

    @Override
    public boolean isAllied() {
        return allied;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * Update the mercenary's state every tick
     */
    @Override
    public void update(Game game) {
        if (mindControlDuration > 0) {
            mindControlDuration--;
            if (mindControlDuration == 0) {
                allied = false;
                isValid = false; // Mark as invalid so that it can be removed from the game
            }
        }
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    /**
     * check whether the current merc can be bribed
     */
    public boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    public void setMindControlDuration(int duration) {
        mindControlDuration = duration;
    }

    /**
     * interact with the player where the player can bribe the merc
     * or mind controls the mercenary (no precedence)
     *
     * @precondition mercernary is interactable with the player
     */
    @Override
    public void interact(Player player, Game game) {
        allied = true;
        if (canBeBribed(player)) {
            player.bribe(bribeAmount);
        } else {
            useScepter(player, game);
        }
        moveAdjacent(player);
    }

    @Override
    public MovementStrategy getMovementStrategy(Player player) {
        if (allied) {
            return new AlliedMovement();
        } else if (player.getEffectivePotion() instanceof InvisibilityPotion) {
            return new RandomMovement();
        } else if (player.getEffectivePotion() instanceof InvincibilityPotion) {
            return new FleeMovement();
        } else {
            return new FollowMovement();
        }
    }

    public void setAdjacentToPlayer() {
        isAdjacentToPlayer = true;
    }

    public boolean isAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    /**
     * If the player does not have enough gold and does not have a sceptre,
     * the mercenary cannot be interacted with.
     */
    @Override
    public boolean isInteractable(Player player) {
        int sceptre = player.countEntityOfType(Sceptre.class);
        return !allied && (canBeBribed(player) || sceptre > 0);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!allied)
            return super.getBattleStatistics();
        return allyStats;
    }

    public void setAllied(boolean allied) {
        this.allied = allied;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public void useScepter(Player player, Game game) {
        Sceptre sceptre = player.get(Sceptre.class);
        mindControlDuration = sceptre.getDuration();
        player.remove(sceptre);
        game.attach(this);
    }

    public void moveAdjacent(Player player) {
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
            isAdjacentToPlayer = true;
    }
}
