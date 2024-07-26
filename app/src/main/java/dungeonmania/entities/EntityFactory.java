package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.entities.buildables.*;
import dungeonmania.entities.collectables.*;
import dungeonmania.entities.enemies.*;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class EntityFactory {
    private JSONObject config;
    private Random ranGen = new Random();

    public EntityFactory(JSONObject config) {
        this.config = config;
    }

    public Entity createEntity(JSONObject jsonEntity) {
        return constructEntity(jsonEntity, config);
    }

    public void spawnSpider(Game game) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        int rate = config.optInt("spider_spawn_interval", 0);
        if (rate == 0 || (tick + 1) % rate != 0)
            return;
        int radius = 20;
        Position player = map.getPlayer().getPosition();

        EntityBuilder builder = new EntityBuilder(config, new Position(0, 0));
        Spider dummySpider = builder.buildSpider(); // for checking possible positions

        List<Position> availablePos = new ArrayList<>();
        for (int i = player.getX() - radius; i < player.getX() + radius; i++) {
            for (int j = player.getY() - radius; j < player.getY() + radius; j++) {
                if (Position.calculatePositionBetween(player, new Position(i, j)).magnitude() > radius)
                    continue;
                Position np = new Position(i, j);
                if (!map.canMoveTo(dummySpider, np) || np.equals(player))
                    continue;
                if (map.getEntities(np).stream().anyMatch(e -> e instanceof Enemy))
                    continue;
                availablePos.add(np);
            }
        }
        Position initPosition = availablePos.get(ranGen.nextInt(availablePos.size()));
        builder.setPosition(initPosition);
        Spider spider = builder.buildSpider();
        map.addEntity(spider);
        game.register(() -> spider.move(game), Game.AI_MOVEMENT, spider.getId());
    }

    public void spawnZombie(Game game, ZombieToastSpawner spawner) {
        GameMap map = game.getMap();
        int tick = game.getTick();
        Random randGen = new Random();
        int spawnInterval = config.optInt("zombie_spawn_interval", ZombieToastSpawner.DEFAULT_SPAWN_INTERVAL);
        if (spawnInterval == 0 || (tick + 1) % spawnInterval != 0)
            return;
        List<Position> pos = spawner.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> !map.getEntities(p).stream().anyMatch(e -> (e instanceof Wall)))
                .collect(Collectors.toList());
        if (pos.size() == 0)
            return;
        EntityBuilder builder = new EntityBuilder(config, pos.get(randGen.nextInt(pos.size())));
        ZombieToast zt = builder.buildZombieToast();
        map.addEntity(zt);
        game.register(() -> zt.move(game), Game.AI_MOVEMENT, zt.getId());
    }

    public Bow buildBow() {
        ItemBuilder builder = new ItemBuilder(config);
        return builder.buildBow();
    }

    public Shield buildShield() {
        ItemBuilder builder = new ItemBuilder(config);
        return builder.buildShield();
    }

    public MidnightArmour buildMidnightArmour() {
        ItemBuilder builder = new ItemBuilder(config);
        return builder.buildMidnightArmour();
    }

    public Sceptre buildSceptre() {
        ItemBuilder builder = new ItemBuilder(config);
        return builder.buildSceptre();
    }

    private Entity constructEntity(JSONObject jsonEntity, JSONObject config) {
        Position pos = new Position(jsonEntity.getInt("x"), jsonEntity.getInt("y"));
        EntityBuilder builder = new EntityBuilder(config, pos);
        builder.setJsonEntity(jsonEntity);

        switch (jsonEntity.getString("type")) {
        case "player":
            return builder.buildPlayer();
        case "zombie_toast":
            return builder.buildZombieToast();
        case "zombie_toast_spawner":
            return builder.buildZombieToastSpawner();
        case "mercenary":
            return builder.buildMercenary();
        case "wall":
            return new Wall(pos);
        case "boulder":
            return new Boulder(pos);
        case "switch":
            return new Switch(pos);
        case "exit":
            return new Exit(pos);
        case "treasure":
            return new Treasure(pos);
        case "wood":
            return new Wood(pos);
        case "arrow":
            return new Arrow(pos);
        case "bomb":
            return builder.buildBomb();
        case "invisibility_potion":
            return builder.buildInvisibilityPotion();
        case "invincibility_potion":
            return builder.buildInvincibilityPotion();
        case "portal":
            return builder.buildPortal();
        case "sword":
            return builder.buildSword();
        case "spider":
            return builder.buildSpider();
        case "door":
            return builder.buildDoor();
        case "key":
            return builder.buildKey();
        case "sun_stone":
            return new SunStone(pos);
        case "hydra":
            return builder.buildHydra();
        default:
            return null;
        }
    }
}
