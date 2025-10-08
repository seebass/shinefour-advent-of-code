import java.util.List;

public class Roboter {

    private EmptyPosition position;

    public Roboter(EmptyPosition position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public boolean move(MovementDirection direction, List<List<Position>> map) {
        int newX = this.position.getX();
        int newY = this.position.getY();

        switch (direction) {
            case UP -> newY--;
            case DOWN -> newY++;
            case LEFT -> newX--;
            case RIGHT -> newX++;
        }

        if (newY < 0 || newY >= map.size() || newX < 0 || newX >= map.getFirst().size()) {
            return false;
        }

        Position targetPosition = map.get(newY).get(newX);

        if (targetPosition instanceof EmptyPosition emptyTargetPos) {
            if (emptyTargetPos.hasBox()) {
                if (!emptyTargetPos.getBox().moveBox(direction, map)) {
                    return false;
                }
            } else if (emptyTargetPos.hasWideBox()) {
                if (!emptyTargetPos.getWideBox().moveBox(direction, map)) {
                    return false;
                }
            }

            this.position = emptyTargetPos;
            return true;
        }

        return false;
    }

}
