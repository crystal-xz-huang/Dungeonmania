package dungeonmania.entities.movement;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class HostileMovement implements MovementStrategy {
    @Override
    public void move(Entity entity, GameMap map, Player player) {
        // Follows the player in a hostile manner by finding the shortest path to the player
        Position nextPos = map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
        map.moveTo(entity, nextPos);
    }
}
