package dungeonmania.entities.buildables;

public class Bow extends Buildable {
    public Bow(int durability) {
        super(null, durability, new BattleStatisticsBuilder().attackMagnifier(2));
    }

}
