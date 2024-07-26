package dungeonmania.entities;

import org.json.JSONObject;

import dungeonmania.battles.BattleStatisticsDirector;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.buildables.MidnightArmour;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.buildables.Shield;

public class ItemBuilder {
    private JSONObject config;
    private BattleStatisticsDirector director;

    public ItemBuilder(JSONObject config) {
        this.config = config;
        this.director = new BattleStatisticsDirector();
    }

    public Bow buildBow() {
        int bowDurability = config.optInt("bow_durability");
        return new Bow(bowDurability, director.constructBowStatistics());
    }

    public Shield buildShield() {
        int shieldDurability = config.optInt("shield_durability");
        double shieldDefence = config.optInt("shield_defence");
        return new Shield(shieldDurability, director.constructShieldStatistics(shieldDefence));
    }

    public MidnightArmour buildMidnightArmour() {
        double midnightArmourAttack = config.optInt("midnight_armour_attack");
        double midnightArmourDefence = config.optInt("midnight_armour_defence");
        return new MidnightArmour(
                director.constructMidnightArmourStatistics(midnightArmourAttack, midnightArmourDefence));
    }

    public Sceptre buildSceptre() {
        int mindControlDuration = config.optInt("mind_control_duration");
        return new Sceptre(mindControlDuration, director.constructSceptreStatistics());
    }
}
