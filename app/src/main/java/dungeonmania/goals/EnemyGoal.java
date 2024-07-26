package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemyGoal implements Goal {
    private int enemyGoal;

    public EnemyGoal(int enemyGoal) {
        this.enemyGoal = enemyGoal;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getPlayer() == null || game.getTick() == 0)
            return false;
        int enemiesDefeated = game.getEnemiesDefeated();
        int spawnerCount = game.getEntities(ZombieToastSpawner.class).size();
        return enemiesDefeated >= enemyGoal && spawnerCount == 0;
    }

    @Override
    public String toString(Game game) {
        return (this.achieved(game) ? "" : ":enemies");
    }
}
