package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.BattleStatisticsBuilder;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.movement.MovementStrategy;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable {
    private BattleStatistics battleStatistics;

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatisticsBuilder().setHealth(health).setAttack(attack)
                .setMagnifier(BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER)
                .setReducer(BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER).build();
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public void move(Game game) {
        Player player = game.getPlayer();
        GameMap map = game.getMap();
        MovementStrategy movementStrategy = getMovementStrategy(player);
        movementStrategy.move(this, map, player);
    }

    public abstract MovementStrategy getMovementStrategy(Player player);

    @Override
    public double getHealth() {
        return battleStatistics.getHealth();
    }

    @Override
    public void setHealth(double health) {
        battleStatistics.setHealth(health);
    }

}
