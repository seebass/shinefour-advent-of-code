public class PointWithDirection extends Point {

    private Direction direction;

    public PointWithDirection(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean equals(PointWithDirection pointWithDirection) {
        return this.getX() == pointWithDirection.getX() &&
                this.getY() == pointWithDirection.getY() &&
                this.direction == pointWithDirection.direction;
    }
}
