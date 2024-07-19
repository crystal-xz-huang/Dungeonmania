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
    private BattleStatistics buff;

    public BattleItem(Position position, int durability, BattleStatistics buff) {
        super(position);
        this.durability = durability;
        this.buff = buff;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.remove(this);
        }
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, buff);
    }

    public abstract boolean isWeapon();

    public BattleStatistics getBuff() {
        return buff;
    }
}
