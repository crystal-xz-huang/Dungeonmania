package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.inventory.Inventory;

// A character with a sceptre does not need to bribe mercenaries to become
// allies, as they can use the sceptre to control their minds
public class Sceptre extends Buildable {
    private int duration;

    public Sceptre(int duration, BattleStatistics buff) {
        super(buff);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    // crafted with (1 wood OR 2 arrows) + (1 key OR 1 treasure OR 1 sunstone) + (1 sun stone)
    public static boolean isBuildable(Inventory inventory) {
        int wood = inventory.count(Wood.class);
        int arrow = inventory.count(Arrow.class);
        int key = inventory.count(Key.class);
        int treasure = inventory.count(Treasure.class);
        int sunStone = inventory.count(SunStone.class);

        return (wood >= 1 || arrow >= 2) && (key >= 1 || treasure >= 1 || sunStone >= 1) && sunStone >= 1;
    }

    public static Sceptre build(EntityFactory factory, Inventory inventory) {
        if (inventory.count(Wood.class) >= 1) {
            inventory.removeFirst(Wood.class);
        } else {
            inventory.removeFirst(Arrow.class);
            inventory.removeFirst(Arrow.class);
        }

        if (inventory.count(Key.class) >= 1) {
            inventory.removeFirst(Key.class);
        } else {
            inventory.removeFirst(Treasure.class);
        }

        inventory.removeFirst(SunStone.class);
        return factory.buildSceptre();
    }

    @Override
    public boolean isWeapon() {
        return false;
    }
}
