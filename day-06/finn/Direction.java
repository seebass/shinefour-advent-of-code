public enum Direction
{
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public static Direction changeDirection(Direction direction) {
        return switch (direction) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }
}
