package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;

public class Bow extends Buildable {
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 2.0;

    public Bow(int durability, BattleStatistics buff) {
        super(durability, buff);
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    // 1 wood + 3 arrows
    public static boolean isBuildable(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int arrow = inventory.count(Arrow.class);
        return wood >= 1 && arrow >= 3;
    }

    public static Bow build(EntityFactory factory, Inventory inventory) {
        inventory.removeFirst(Wood.class);
        inventory.removeFirst(Arrow.class);
        inventory.removeFirst(Arrow.class);
        inventory.removeFirst(Arrow.class);
        return factory.buildBow();
    }
}
