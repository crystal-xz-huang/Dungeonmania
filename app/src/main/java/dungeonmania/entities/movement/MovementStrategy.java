package dungeonmania.entities.movement;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;

public interface MovementStrategy {
    void move(Entity entity, GameMap map, Player player);
}
