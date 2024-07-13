package dungeonmania.entities.movement;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class FollowMovement implements MovementStrategy {
    @Override
    public void move(Entity entity, GameMap map, Player player) {
        Position nextPos = map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
        map.moveTo(entity, nextPos);
    }
}
