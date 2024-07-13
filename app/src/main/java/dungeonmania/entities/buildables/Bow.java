package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;

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
    @Override
    public boolean canBuild(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int arrows = inventory.count(Arrow.class);
        return wood >= 1 && arrows >= 3;
    }
}
