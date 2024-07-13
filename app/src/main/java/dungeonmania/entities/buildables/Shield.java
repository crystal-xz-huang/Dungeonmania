package dungeonmania.entities.buildables;

public class Shield extends Buildable {
    public Shield(int durability, double defence) {
        super(null, durability, new BattleStatisticsBuilder().defence(defence));
    }
}
