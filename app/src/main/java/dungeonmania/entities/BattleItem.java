package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

/**
 * Item has buff in battles
 */
public abstract class BattleItem extends InventoryItem {
    private int durability;

    public BattleItem(Position position, int durability) {
        super(position);
        this.durability = durability;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    public int getDurability() {
        return durability;
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, getBattleStatistics());
    }

    public abstract boolean isWeapon();

    public abstract BattleStatistics getBattleStatistics();

}
