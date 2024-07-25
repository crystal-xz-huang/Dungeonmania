package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import dungeonmania.Game;
import dungeonmania.battles.*;
import dungeonmania.entities.buildables.Buildable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.*;
import dungeonmania.entities.enemies.*;
import dungeonmania.entities.inventory.*;
import dungeonmania.entities.playerState.*;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Battleable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private Inventory inventory;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;

    private PlayerState state;

    public Player(Position position, BattleStatistics stats) {
        super(position, stats);
        state = new BaseState(this);
        inventory = new Inventory();
    }

    public int getCollectedTreasureCount() {
        return inventory.count(Treasure.class) + inventory.count(SunStone.class);
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    // cannnot modify signature
    public List<String> getBuildables() {
        return inventory.getBuildables();
    }

    public boolean build(String entity, EntityFactory factory, Game game) {
        InventoryItem item = inventory.checkBuildCriteria(entity, factory);
        if (item == null)
            return false;
        return inventory.add(item);
    }

    public void move(GameMap map, Direction direction) {
        this.setFacing(direction);
        map.moveTo(this, Position.translateBy(this.getPosition(), direction));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Enemy && !((Enemy) entity).isAllied()) {
            map.getGame().battle(this, (Enemy) entity);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public boolean hasKey(int number) {
        Key key = inventory.getFirst(Key.class);
        SunStone sunstone = inventory.getFirst(SunStone.class);
        return (key != null && key.getnumber() == number) || sunstone != null;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public boolean pickUp(Entity item) {
        if (item instanceof InventoryItem && !(item instanceof Buildable)) {
            return inventory.add((InventoryItem) item);
        }
        return false;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Potion getEffectivePotion() {
        return inEffective;
    }

    // can only bribe merc with treasure but not sunstone
    public void bribe(int bribeAmount) {
        for (int i = 0; i < bribeAmount; i++) {
            inventory.removeFirst(Treasure.class);
        }
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;
            state = new BaseState(this);
            return;
        }
        inEffective = queue.remove();
        if (inEffective instanceof InvincibilityPotion) {
            state = new InvincibleState(this);
        } else {
            state = new InvisibleState(this);
        }
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void use(Potion potion, int tick) {
        inventory.remove(potion);
        queue.add(potion);
        if (inEffective == null) {
            triggerNext(tick);
        }
    }

    public void onTick(int tick) {
        if (inEffective == null || tick == nextTrigger) {
            triggerNext(tick);
        }
    }

    public void remove(InventoryItem item) {
        inventory.remove(item);
    }

    public <T extends InventoryItem> T get(Class<T> itemType) {
        return inventory.getFirst(itemType);
    }

    public <T extends InventoryItem> void remove(Class<T> itemType) {
        inventory.removeFirst(itemType);
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    public BattleStatistics applyBuff(Game game) {
        return state.applyBuff(game);
    }

    public List<BattleItem> getUsedBattleItems() {
        return state.getUsedBattleItems();
    }
}
