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
        if (game == null)
            return false;
        int enemiesDefeated = game.getEnemiesDefeated();
        int spawnerCount = game.getEntities(ZombieToastSpawner.class).size();
        if (enemiesDefeated >= enemyGoal && spawnerCount == 0)
            return true;
        return false;
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":enemies";
    }
}
