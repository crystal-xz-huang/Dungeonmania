package dungeonmania.entities.buildables;

import dungeonmania.entities.inventory.Inventory;

/**
 * Not creatable from Dungeon Map
 * Must be built by player
 * Has no position
 */
public interface Buildable {
    public boolean canBuild(Inventory inventory);
}
