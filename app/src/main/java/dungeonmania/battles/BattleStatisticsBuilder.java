package dungeonmania.battles;

public class BattleStatisticsBuilder {
    private double health;
    private double attack;
    private double defence;
    private double magnifier = 1; // Default value
    private double reducer = 1; // Default value
    private boolean invincible = false; // Default value
    private boolean enabled = true; // Default value

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

    public BattleStatistics build() {
        return new BattleStatistics(health, attack, defence, magnifier, reducer, invincible, enabled);
    }
}
