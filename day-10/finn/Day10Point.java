public class Day10Point {

    private final int x;
    private final int y;

    public Day10Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Day10Point point) {
            return this.x == point.getX() && this.y == point.getY();
        }
        return false;
    }

}
