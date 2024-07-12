package dungeonmania.entities.movement;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class FleeMovement implements MovementStrategy {
    @Override
    public void move(Entity entity, GameMap map, Player player) {
        Position plrDiff = Position.calculatePositionBetween(player.getPosition(), entity.getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(entity.getPosition(), Direction.RIGHT)
                : Position.translateBy(entity.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(entity.getPosition(), Direction.UP)
                : Position.translateBy(entity.getPosition(), Direction.DOWN);

        Position nextPos = getNextPosition(entity, map, plrDiff, moveX, moveY);
        map.moveTo(entity, nextPos);
    }

    private Position getNextPosition(Entity entity, GameMap map, Position plrDiff, Position moveX, Position moveY) {
        Position offset = entity.getPosition();
        List<Position> potentialMoves = new ArrayList<>();

        if (plrDiff.getY() == 0) {
            potentialMoves.add(moveX);
        } else if (plrDiff.getX() == 0) {
            potentialMoves.add(moveY);
        } else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            potentialMoves.add(moveX);
            potentialMoves.add(moveY);
        } else {
            potentialMoves.add(moveY);
            potentialMoves.add(moveX);
        }

        for (Position move : potentialMoves) {
            if (map.canMoveTo(entity, move)) {
                offset = move;
                break;
            }
        }

        return offset;
    }
}
