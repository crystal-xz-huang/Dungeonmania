package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Buildable extends Entity implements InventoryItem, BattleItem {
    private int durability;
    private BattleStatisticsBuilder statsBuilder;

    public Buildable(Position position, int durability, BattleStatisticsBuilder statsBuilder) {
        super(position);
        this.durability = durability;
        this.statsBuilder = statsBuilder;
    }

    @Override
    public void use(Game game) {
        durability--;
        if (durability <= 0) {
            game.getPlayer().remove(this);
        }
    }

    @Override
    public int getDurability() {
        return durability;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, getBattleStatistics());
    }

    private BattleStatistics getBattleStatistics() {
        return statsBuilder.build();
    }

}
