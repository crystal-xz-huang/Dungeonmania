package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.Shield;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {
        List<String> result = new ArrayList<>();
        if (Bow.isBuildable(this)) {
            result.add("bow");
        }
        if (Shield.isBuildable(this)) {
            result.add("shield");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, String entity, EntityFactory factory) {
        if (entity.equals("bow") && Bow.isBuildable(this)) {
            return Bow.build(factory, this);
        } else if (entity.equals("shield") && Shield.isBuildable(this)) {
            return Shield.build(factory, this);
        }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> void removeFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item)) {
                items.remove(item);
                return;
            }
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return items.stream().anyMatch(i -> i instanceof BattleItem && ((BattleItem) i).isWeapon());
    }

    public BattleItem getWeapon() {
        return items.stream().filter(i -> i instanceof BattleItem && ((BattleItem) i).isWeapon())
                .map(BattleItem.class::cast).findFirst().orElse(null);
    }
}
