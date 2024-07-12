package dungeonmania.entities.movement;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMovement implements MovementStrategy {
    @Override
    public void move(Entity entity, GameMap map, Player player) {
        boolean isAdjacentToPlayer = Position.isAdjacent(entity.getPosition(), player.getPosition());
        Position nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                : map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
        map.moveTo(entity, nextPos);
    }
}
