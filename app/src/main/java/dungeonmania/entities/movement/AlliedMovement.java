package dungeonmania.entities.movement;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMovement implements MovementStrategy {
    @Override
    public void move(Entity entity, GameMap map, Player player) {
        Mercenary mercenary = (Mercenary) entity;
        boolean isAdjacentToPlayer = mercenary.isAdjacentToPlayer();
        Position nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                : map.dijkstraPathFind(mercenary.getPosition(), player.getPosition(), mercenary);
        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), nextPos)) {
            mercenary.setAdjacentToPlayer();
        }
        map.moveTo(mercenary, nextPos);
    }
}
