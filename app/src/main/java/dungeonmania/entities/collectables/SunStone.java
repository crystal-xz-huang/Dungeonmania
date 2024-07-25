package dungeonmania.entities.collectables;

import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

// SunStone is a subclass of Treasure
// Used interchangeably with treasure or keys when building entities
// Can be used to open doors
public class SunStone extends InventoryItem {
    public SunStone(Position position) {
        super(position);
    }
}
