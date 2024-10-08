package dungeonmania.entities.enemies;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.movement.MovementStrategy;
import dungeonmania.entities.movement.SpiderMovement;
import dungeonmania.util.Position;

public class Spider extends Enemy {
    public static final int DEFAULT_SPAWN_RATE = 0;
    public static final double DEFAULT_ATTACK = 5;
    public static final double DEFAULT_HEALTH = 10;
    private MovementStrategy movementStrategy;

    public Spider(Position position, BattleStatistics stats) {
        super(position.asLayer(Entity.DOOR_LAYER + 1), stats);
        /**
         * Establish spider movement trajectory Spider moves as follows:
         *  8 1 2       10/12  1/9  2/8
         *  7 S 3       11     S    3/7
         *  6 5 4       B      5    4/6
         */
        this.movementStrategy = new SpiderMovement(position);
    };

    @Override
    public MovementStrategy getMovementStrategy(Player player) {
        return movementStrategy;
    }

}
