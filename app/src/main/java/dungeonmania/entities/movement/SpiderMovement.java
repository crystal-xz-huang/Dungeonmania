package dungeonmania.entities.movement;

import java.util.List;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SpiderMovement implements MovementStrategy {
    private List<Position> movementTrajectory;
    private int nextPositionElement;
    private boolean forward;

    public SpiderMovement(Position position) {
        movementTrajectory = position.getAdjacentPositions();
        nextPositionElement = 1;
        forward = true;
    }

    private void updateNextPosition() {
        if (forward) {
            nextPositionElement++;
            if (nextPositionElement == 8) {
                nextPositionElement = 0;
            }
        } else {
            nextPositionElement--;
            if (nextPositionElement == -1) {
                nextPositionElement = 7;
            }
        }
    }

    @Override
    public void move(Entity entity, GameMap map, Player player) {
        Position nextPos = movementTrajectory.get(nextPositionElement);
        List<Entity> entities = map.getEntities(nextPos);
        if (entities != null && entities.size() > 0 && entities.stream().anyMatch(e -> e instanceof Boulder)) {
            forward = !forward;
            updateNextPosition();
            updateNextPosition();
        }
        nextPos = movementTrajectory.get(nextPositionElement);
        entities = map.getEntities(nextPos);
        if (entities == null || entities.size() == 0 || entities.stream().allMatch(e -> e.canMoveOnto(map, entity))) {
            map.moveTo(entity, nextPos);
            updateNextPosition();
        }
    }
}
