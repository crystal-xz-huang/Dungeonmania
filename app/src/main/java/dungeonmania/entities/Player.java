package dungeonmania.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;

import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.playerState.BaseState;
import dungeonmania.entities.playerState.InvincibleState;
import dungeonmania.entities.playerState.InvisibleState;
import dungeonmania.entities.playerState.PlayerState;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Player extends Entity implements Battleable {
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 5.0;
    private BattleStatistics battleStatistics;
    private Inventory inventory;
    private Queue<Potion> queue = new LinkedList<>();
    private Potion inEffective = null;
    private int nextTrigger = 0;
    private PlayerState state;
    private List<BattleItem> usedBattleItems = new ArrayList<>();

    public Player(Position position, double health, double attack) {
        super(position);
        battleStatistics = new BattleStatisticsBuilder().setHealth(health).setAttack(attack)
                .setMagnifier(BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER)
                .setReducer(BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER).build();
        inventory = new Inventory();
        state = new BaseState(this);
    }

    public int getCollectedTreasureCount() {
        return inventory.count(Treasure.class);
    }

    public boolean hasWeapon() {
        return inventory.hasWeapon();
    }

    public List<String> getBuildables() {
        return inventory.getBuildables();
    }

    public boolean build(String entity, EntityFactory factory) {
        InventoryItem item = inventory.checkBuildCriteria(this, entity, factory);
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
        if (entity instanceof Enemy) {
            if (entity instanceof Mercenary) {
                if (((Mercenary) entity).isAllied())
                    return;
            }
            map.getGame().battle(this, (Enemy) entity);
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public Entity getEntity(String itemUsedId) {
        return inventory.getEntity(itemUsedId);
    }

    public boolean pickUp(Entity item) {
        if (item instanceof InventoryItem) {
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

    public <T extends InventoryItem> void use(Class<T> itemType) {
        T item = inventory.getFirst(itemType);
        if (item != null) {
            inventory.remove(item);
        }
    }

    public void use(Bomb bomb, GameMap map) {
        inventory.remove(bomb);
        bomb.onPutDown(map, getPosition());
    }

    public void triggerNext(int currentTick) {
        if (queue.isEmpty()) {
            inEffective = null;
            changeState(new BaseState(this));
            return;
        }
        inEffective = queue.remove();
        if (inEffective instanceof InvincibilityPotion) {
            changeState(new InvincibleState(this));
        } else {
            changeState(new InvisibleState(this));
        }
        nextTrigger = currentTick + inEffective.getDuration();
    }

    public void changeState(PlayerState playerState) {
        state = playerState;
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

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    public <T extends InventoryItem> int countEntityOfType(Class<T> itemType) {
        return inventory.count(itemType);
    }

    @Override
    public double getHealth() {
        return battleStatistics.getHealth();
    }

    @Override
    public void setHealth(double health) {
        battleStatistics.setHealth(health);
    }

    public BattleStatistics applyBuff(Game game) {
        BattleStatistics buffedStatistics = applyPotionBuff(battleStatistics);
        buffedStatistics = applyItemBuff(game, buffedStatistics);
        buffedStatistics = applyAlliedMercenaryBuffs(game, buffedStatistics);
        return buffedStatistics;
    }

    private BattleStatistics applyAlliedMercenaryBuffs(Game game, BattleStatistics origin) {
        List<Mercenary> allies = game.getEntities(Mercenary.class).stream().filter(Mercenary::isAllied)
                .collect(Collectors.toList());
        for (Mercenary merc : allies) {
            origin = BattleStatistics.applyBuff(origin, merc.getBattleStatistics());
        }
        return origin;
    }

    private BattleStatistics applyItemBuff(Game game, BattleStatistics origin) {
        for (BattleItem item : inventory.getEntities(BattleItem.class)) {
            origin = item.applyBuff(origin);
            usedBattleItems.add(item);
            item.use(game);
        }
        return origin;
    }

    private BattleStatistics applyPotionBuff(BattleStatistics origin) {
        Potion effectivePotion = getEffectivePotion();
        if (effectivePotion != null) {
            origin = state.applyBuff(origin);
        }
        return origin;
    }

    public List<BattleItem> getUsedBattleItems() {
        // return a copy of the used items
        List<BattleItem> copy = new ArrayList<>(usedBattleItems);
        usedBattleItems.clear();
        return copy;
    }
}
