package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.EntityFactory;

public class Bow extends BattleItem implements Buildable {
    public Bow(int durability) {
        super(null, durability);
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setMagnifier(2).build();
    }

    @Override
    public boolean isWeapon() {
        return true;
    }

    // 1 wood + 3 arrows
    public static boolean isBuildable(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int arrows = inventory.count(Arrow.class);
        return wood >= 1 && arrows >= 3;
    }

    public static Bow build(EntityFactory factory, Inventory inventory) {
        inventory.removeFirst(Wood.class);
        inventory.removeFirst(Arrow.class);
        inventory.removeFirst(Arrow.class);
        inventory.removeFirst(Arrow.class);
        return factory.buildBow();
    }
}
