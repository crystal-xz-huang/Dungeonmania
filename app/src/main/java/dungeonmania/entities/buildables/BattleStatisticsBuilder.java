package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;

public class BattleStatisticsBuilder {
    private double defence;
    private double health;
    private double attack;
    private double attackMagnifier;
    private double damageReducer;

    public BattleStatisticsBuilder() {
        this.defence = 0;
        this.health = 0;
        this.attack = 0;
        this.attackMagnifier = 1;
        this.damageReducer = 1;
    }

    public BattleStatisticsBuilder defence(double defence) {
        this.defence = defence;
        return this;
    }

    public BattleStatisticsBuilder health(double health) {
        this.health = health;
        return this;
    }

    public BattleStatisticsBuilder attack(double attack) {
        this.attack = attack;
        return this;
    }

    public BattleStatisticsBuilder attackMagnifier(double attackMagnifier) {
        this.attackMagnifier = attackMagnifier;
        return this;
    }

    public BattleStatisticsBuilder damageReducer(double damageReducer) {
        this.damageReducer = damageReducer;
        return this;
    }

    public double getDefence() {
        return defence;
    }

    public double getHealth() {
        return health;
    }

    public double getAttack() {
        return attack;
    }

    public double getAttackMagnifier() {
        return attackMagnifier;
    }

    public double getDamageReducer() {
        return damageReducer;
    }

    public BattleStatistics build() {
        return new BattleStatistics(health, attack, defence, attackMagnifier, damageReducer);
    }
}
