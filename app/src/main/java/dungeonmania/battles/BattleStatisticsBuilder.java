package dungeonmania.battles;

public class BattleStatisticsBuilder {
    private double health;
    private double attack;
    private double defence;
    private double magnifier = 1;
    private double reducer = 1;
    private boolean invincible = false;
    private boolean enabled = true;
    private double healthIncreaseRate = 0;
    private double healthIncreaseAmount = 0;

    public BattleStatisticsBuilder setHealth(double health) {
        this.health = health;
        return this;
    }

    public BattleStatisticsBuilder setAttack(double attack) {
        this.attack = attack;
        return this;
    }

    public BattleStatisticsBuilder setDefence(double defence) {
        this.defence = defence;
        return this;
    }

    public BattleStatisticsBuilder setMagnifier(double magnifier) {
        this.magnifier = magnifier;
        return this;
    }

    public BattleStatisticsBuilder setReducer(double reducer) {
        this.reducer = reducer;
        return this;
    }

    public BattleStatisticsBuilder setInvincible(boolean invincible) {
        this.invincible = invincible;
        return this;
    }

    public BattleStatisticsBuilder setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public BattleStatisticsBuilder setHealthIncreaseRate(double healthIncreaseRate) {
        this.healthIncreaseRate = healthIncreaseRate;
        return this;
    }

    public BattleStatisticsBuilder setHealthIncreaseAmount(double healthIncreaseAmount) {
        this.healthIncreaseAmount = healthIncreaseAmount;
        return this;
    }

    public BattleStatistics build() {
        return new BattleStatistics(health, attack, defence, magnifier, reducer, invincible, enabled,
                healthIncreaseRate, healthIncreaseAmount);
    }
}
