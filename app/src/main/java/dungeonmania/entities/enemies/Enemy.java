package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.movement.MovementStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Battleable {
    public Enemy(Position position, BattleStatistics stats) {
        super(position.asLayer(Entity.CHARACTER_LAYER), stats);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public void move(Game game) {
        Player player = game.getPlayer();
        GameMap map = game.getMap();
        MovementStrategy movementStrategy = getMovementStrategy(player);
        movementStrategy.move(this, map, player);
    }

    public abstract MovementStrategy getMovementStrategy(Player player);

    public boolean isAllied() {
        return false;
    }

}
