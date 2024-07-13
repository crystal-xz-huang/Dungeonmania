package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.inventory.Inventory;

public class Shield extends BattleItem implements Buildable {
    private double defence;

    public Shield(int durability, double defence) {
        super(null, durability);
        this.defence = defence;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return new BattleStatisticsBuilder().setDefence(defence).build();
    }

    @Override
    public boolean isWeapon() {
        return false;
    }

    // 2 wood + (1 treasure OR 1 key)
    @Override
    public boolean canBuild(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int treasure = inventory.count(Treasure.class);
        int key = inventory.count(Key.class);
        return wood >= 2 && (treasure >= 1 || key >= 1);
    }
}
