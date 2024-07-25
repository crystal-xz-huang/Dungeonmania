package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.inventory.Inventory;

public class MidnightArmour extends Buildable {
    public MidnightArmour(BattleStatistics buff) {
        super(buff);
    }

    // crafted with (1 sword + 1 sun stone) if there are no zombies currently in the dungeon
    public static boolean isBuildable(Inventory inventory) {
        int sunstone = inventory.count(SunStone.class);
        int sword = inventory.count(Sword.class);
        return sunstone >= 1 && sword >= 1;
    }

    public static MidnightArmour build(EntityFactory factory, Inventory inventory) {
        inventory.removeFirst(SunStone.class);
        inventory.removeFirst(Sword.class);
        return factory.buildMidnightArmour();
    }

    @Override
    public boolean isWeapon() {
        return false;
    }
}
