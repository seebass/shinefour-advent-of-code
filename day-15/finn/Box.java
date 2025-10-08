import java.util.List;

public class Box {

    private EmptyPosition position;

    public Box(EmptyPosition position) {
        this.position = position;
    }

    public boolean moveBox(MovementDirection direction, List<List<Position>> map) {
        int newX = this.position.getX();
        int newY = this.position.getY();

        switch (direction) {
            case UP -> newY = newY -1;
            case DOWN -> newY = newY +1;
            case LEFT -> newX = newX -1;
            case RIGHT -> newX = newX +1;
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
            }

            this.position.setBox(null);
            emptyTargetPos.setBox(this);
            this.position = emptyTargetPos;
            return true;
        }

        return false;
    }

}
