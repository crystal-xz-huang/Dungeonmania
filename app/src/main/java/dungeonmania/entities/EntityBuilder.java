package dungeonmania.entities;

import org.json.JSONObject;

import dungeonmania.battles.BattleStatisticsDirector;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.Assassin;
import dungeonmania.entities.enemies.Hydra;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.util.Position;

public class EntityBuilder {
    private JSONObject config;
    private JSONObject jsonEntity;
    private Position pos;
    private BattleStatisticsDirector director;

    public EntityBuilder(JSONObject config, Position pos) {
        this.config = config;
        this.pos = pos;
        this.director = new BattleStatisticsDirector();
    }

    public void setJsonEntity(JSONObject jsonEntity) {
        this.jsonEntity = jsonEntity;
    }

    public void setPosition(Position pos) {
        this.pos = pos;
    }

    public Player buildPlayer() {
        double playerHealth = config.optDouble("player_health", Player.DEFAULT_HEALTH);
        double playerAttack = config.optDouble("player_attack", Player.DEFAULT_ATTACK);
        return new Player(pos, director.constructPlayerStatistics(playerHealth, playerAttack));
    }

    public Spider buildSpider() {
        double spiderHealth = config.optDouble("spider_health", Spider.DEFAULT_HEALTH);
        double spiderAttack = config.optDouble("spider_attack", Spider.DEFAULT_ATTACK);
        return new Spider(pos, director.constructEnemyStatistics(spiderHealth, spiderAttack));
    }

    public ZombieToast buildZombieToast() {
        double zombieHealth = config.optDouble("zombie_health", ZombieToast.DEFAULT_HEALTH);
        double zombieAttack = config.optDouble("zombie_attack", ZombieToast.DEFAULT_ATTACK);
        return new ZombieToast(pos, director.constructEnemyStatistics(zombieHealth, zombieAttack));
    }

    public ZombieToastSpawner buildZombieToastSpawner() {
        int zombieSpawnRate = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        return new ZombieToastSpawner(pos, zombieSpawnRate);
    }

    public Mercenary buildMercenary() {
        double mercenaryHealth = config.optDouble("mercenary_health", Mercenary.DEFAULT_HEALTH);
        double mercenaryAttack = config.optDouble("mercenary_attack", Mercenary.DEFAULT_ATTACK);
        double allyAttack = config.optDouble("ally_attack", Mercenary.DEFAULT_HEALTH);
        double allyDefence = config.optDouble("ally_defence", Mercenary.DEFAULT_ATTACK);
        int mercenaryBribeAmount = config.optInt("bribe_amount", Mercenary.DEFAULT_BRIBE_AMOUNT);
        int mercenaryBribeRadius = config.optInt("bribe_radius", Mercenary.DEFAULT_BRIBE_RADIUS);
        return new Mercenary(pos, director.constructEnemyStatistics(mercenaryHealth, mercenaryAttack),
                mercenaryBribeAmount, mercenaryBribeRadius, director.constructAllyStatistics(allyAttack, allyDefence));
    }

    public Assassin buildAssassin() {
        double assassinHealth = config.optDouble("assassin_health", Assassin.DEFAULT_HEALTH);
        double assassinAttack = config.optDouble("assassin_attack", Assassin.DEFAULT_ATTACK);
        double allyAttack = config.optDouble("ally_attack", Mercenary.DEFAULT_HEALTH);
        double allyDefence = config.optDouble("ally_defence", Mercenary.DEFAULT_ATTACK);
        int assassinBribeAmount = config.optInt("assassin_bribe_amount", Assassin.DEFAULT_BRIBE_AMOUNT);
        double assassinBribeFailRate = config.optDouble("assassin_bribe_fail_rate", Assassin.DEFAULT_BRIBE_FAIL_RATE);
        int assassinBribeRadius = config.optInt("bribe_radius", Mercenary.DEFAULT_BRIBE_RADIUS);
        return new Assassin(pos, director.constructEnemyStatistics(assassinHealth, assassinAttack), assassinBribeAmount,
                assassinBribeRadius, assassinBribeFailRate, director.constructAllyStatistics(allyAttack, allyDefence));
    }

    public Hydra buildHydra() {
        double hydraHealthIncreaseRate = config.optDouble("hydra_health_increase_rate",
                Hydra.DEFAULT_HEALTH_INCREASE_RATE);
        double hydraHealthIncreaseAmount = config.optDouble("hydra_health_increase_amount",
                Hydra.DEFAULT_HEALTH_INCREASE_AMOUNT);
        return new Hydra(pos, director.constructHydraStatistics(hydraHealthIncreaseRate, hydraHealthIncreaseAmount));
    }

    public Bomb buildBomb() {
        int bombRadius = config.optInt("bomb_radius", Bomb.DEFAULT_RADIUS);
        return new Bomb(pos, bombRadius);
    }

    public InvincibilityPotion buildInvincibilityPotion() {
        int invincibilityPotionDuration = config.optInt("invincibility_potion_duration",
                InvincibilityPotion.DEFAULT_DURATION);
        return new InvincibilityPotion(pos, invincibilityPotionDuration, director.constructInvincibilityStatistics());
    }

    public InvisibilityPotion buildInvisibilityPotion() {
        int invisibilityPotionDuration = config.optInt("invisibility_potion_duration",
                InvisibilityPotion.DEFAULT_DURATION);
        return new InvisibilityPotion(pos, invisibilityPotionDuration, director.constructInvisibilityStatistics());
    }

    public Portal buildPortal() {
        return new Portal(pos, ColorCodedType.valueOf(jsonEntity.getString("colour")));
    }

    public Sword buildSword() {
        double swordAttack = config.optDouble("sword_attack", Sword.DEFAULT_ATTACK);
        int swordDurability = config.optInt("sword_durability", Sword.DEFAULT_DURABILITY);
        return new Sword(pos, swordDurability, director.constructSwordStatistics(swordAttack));
    }

    public Door buildDoor() {
        return new Door(pos, jsonEntity.getInt("key"));
    }

    public Key buildKey() {
        return new Key(pos, jsonEntity.getInt("key"));
    }
}
