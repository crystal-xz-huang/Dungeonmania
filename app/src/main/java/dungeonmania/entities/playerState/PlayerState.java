package dungeonmania.entities.playerState;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.Inventory;

import java.util.stream.Collectors;
import java.util.*;

public abstract class PlayerState {
    private Player player;
    private List<BattleItem> usedBattleItems = new ArrayList<>();

    public PlayerState(Player player) {
        this.player = player;
    }

    public abstract BattleStatistics getStateStatistics();

    public BattleStatistics applyBuff(Game game) {
        BattleStatistics origin = player.getBattleStatistics();
        Potion effectivePotion = player.getEffectivePotion();
        if (effectivePotion != null) {
            origin = BattleStatistics.applyBuff(origin, getStateStatistics());
        }

        origin = applyItemBuff(game, origin);
        origin = applyAlliedMercenaryBuffs(game, origin);
        return origin;
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
        Inventory inventory = player.getInventory();
        for (BattleItem item : inventory.getEntities(BattleItem.class)) {
            origin = item.applyBuff(origin);
            usedBattleItems.add(item);
            item.use(game);
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
