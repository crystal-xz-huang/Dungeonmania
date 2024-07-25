package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.EntityFactory;

public class Shield extends Buildable {
    public Shield(int durability, BattleStatistics buff) {
        super(durability, buff);
    }

    @Override
    public boolean isWeapon() {
        return false;
    }

    // 2 wood + (1 treasure OR 1 key OR 1 sunstone)
    public static boolean isBuildable(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int treasure = inventory.count(Treasure.class);
        int key = inventory.count(Key.class);
        int sunstone = inventory.count(SunStone.class);
        return wood >= 2 && (treasure >= 1 || key >= 1 || sunstone >= 1);
    }

    public static Shield build(EntityFactory factory, Inventory inventory) {
        inventory.removeFirst(Wood.class);
        inventory.removeFirst(Wood.class);

        if (inventory.count(Treasure.class) >= 1) {
            inventory.removeFirst(Treasure.class);
        } else if (inventory.count(Key.class) >= 1) {
            inventory.removeFirst(Key.class);
        }
        return factory.buildShield();
    }
}
