package dungeonmania.battles;

import dungeonmania.entities.buildables.Bow;

public class BattleStatisticsDirector {
    public BattleStatistics constructBaseStatistics() {
        return new BattleStatisticsBuilder().build();
    }

    public BattleStatistics constructEnemyStatistics(double health, double attack) {
        return new BattleStatisticsBuilder().setHealth(health).setAttack(attack)
                .setMagnifier(BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER)
                .setReducer(BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER).build();
    }

    public BattleStatistics constructPlayerStatistics(double health, double attack) {
        return new BattleStatisticsBuilder().setHealth(health).setAttack(attack)
                .setMagnifier(BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER)
                .setReducer(BattleStatistics.DEFAULT_PLAYER_DAMAGE_REDUCER).build();
    }

    public BattleStatistics constructAllyStatistics(double allyAttack, double allyDefence) {
        return new BattleStatisticsBuilder().setAttack(allyAttack).setDefence(allyDefence).build();
    }

    public BattleStatistics constructBowStatistics() {
        return new BattleStatisticsBuilder().setMagnifier(Bow.DEFAULT_ATTACK_SCALE_FACTOR).build();
    }

    public BattleStatistics constructSwordStatistics(double attack) {
        return new BattleStatisticsBuilder().setAttack(attack).build();
    }

    public BattleStatistics constructShieldStatistics(double defence) {
        return new BattleStatisticsBuilder().setDefence(defence).build();
    }

    public BattleStatistics constructMidnightArmourStatistics(double attack, double defence) {
        return new BattleStatisticsBuilder().setAttack(attack).setDefence(defence).build();
    }

    public BattleStatistics constructInvincibilityStatistics() {
        return new BattleStatisticsBuilder().setInvincible(true).build();
    }

    public BattleStatistics constructInvisibilityStatistics() {
        return new BattleStatisticsBuilder().setEnabled(false).build();
    }

    public BattleStatistics constructSceptreStatistics() {
        return new BattleStatisticsBuilder().build();
    }
}
