package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

/**
 * Not creatable from Dungeon Map
 * Must be built by player
 * Has no position
 */
public abstract class Buildable extends BattleItem {
    public Buildable(BattleStatistics buff) {
        super(null, buff);
    }

    public Buildable(int durability, BattleStatistics buff) {
        super(null, durability, buff);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        return;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return false;
    }
}
