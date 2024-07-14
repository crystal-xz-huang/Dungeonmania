package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.response.models.RoundResponse;
import dungeonmania.util.NameConverter;

public class BattleFacade {
    private List<BattleResponse> battleResponses = new ArrayList<>();

    public void battle(Game game, Player player, Enemy enemy) {
        // 0. init
        double initialPlayerHealth = player.getHealth();
        double initialEnemyHealth = enemy.getHealth();
        String enemyString = NameConverter.toSnakeCase(enemy);

        // 1. apply buff provided by the game and player's inventory
        BattleStatistics playerBattleStatistics = player.applyBuff(game);

        // 2. Battle the two stats
        BattleStatistics enemyBattleStatistics = enemy.getBattleStatistics();
        if (!playerBattleStatistics.isEnabled() || !enemyBattleStatistics.isEnabled())
            return;
        List<BattleRound> rounds = BattleStatistics.battle(playerBattleStatistics, enemyBattleStatistics);

        // 3. update health to the actual statistics
        player.setHealth(playerBattleStatistics.getHealth());
        enemy.setHealth(enemyBattleStatistics.getHealth());

        // 4. get the player's used battle items
        List<BattleItem> usedBattleItems = player.getUsedBattleItems();

        // 5. Log the battle - solidate it to be a battle response
        List<RoundResponse> roundResponses = buildRoundResponses(rounds);
        List<ItemResponse> itemResponses = buildItemResponses(usedBattleItems);
        battleResponses.add(new BattleResponse(enemyString, roundResponses, itemResponses, initialPlayerHealth,
                initialEnemyHealth));
    }

    public List<BattleResponse> getBattleResponses() {
        return battleResponses;
    }

    private List<RoundResponse> buildRoundResponses(List<BattleRound> rounds) {
        return rounds.stream().map(ResponseBuilder::getRoundResponse).collect(Collectors.toList());
    }

    private List<ItemResponse> buildItemResponses(List<BattleItem> battleItems) {
        return battleItems.stream().map(Entity.class::cast).map(ResponseBuilder::getItemResponse)
                .collect(Collectors.toList());
    }
}
